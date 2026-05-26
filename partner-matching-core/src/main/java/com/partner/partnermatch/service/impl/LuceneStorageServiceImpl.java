package com.partner.partnermatch.service.impl;

import ai.onnxruntime.OrtException;
import com.partner.partnermatch.pojo.LuceneSearchResult;
import com.partner.partnermatch.service.LuceneStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
public class LuceneStorageServiceImpl implements LuceneStorageService {
    private String storagePath = "./rag_index";
    @Autowired private RAGTransferServiceImpl ragTransferService;

    private final Directory directory;
    private final IndexWriter writer;
    private IndexReader reader;
    private IndexSearcher searcher;

    public LuceneStorageServiceImpl() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig();
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        directory = FSDirectory.open(Path.of(storagePath));
        writer = new IndexWriter(directory, config);
        refreshReader();
    }

    private void refreshReader() throws IOException {
        if(reader!=null){
            reader.close();
        }
        reader = DirectoryReader.open(writer);
        searcher = new IndexSearcher(reader);
    }

    private void updateDocument(long userId, Document doc) throws IOException {
        writer.updateDocument(userTerm(userId), doc);
        writer.commit();
        refreshReader();
    }
    private Term userTerm(long userId) {
        return new Term("userId", String.valueOf(userId));
    }

    public Document searchById(long userId) throws IOException {
        TopDocs results = searcher.search(new TermQuery(userTerm(userId)), 1);
        return searcher.storedFields().document(results.scoreDocs[0].doc);
    }

    public void updateUserTags(long userId, String[] tags) throws OrtException, IOException {
        Document doc = new Document();
        doc.add(new StringField("userId", String.valueOf(userId), Field.Store.YES));
        for(int i = 0; i < tags.length; i++){
            doc.add(new StringField("tag", tags[i], Field.Store.YES));
        }
        StringBuilder builder = new StringBuilder();
        for (String tag : tags) {
            builder.append(tag).append(" ");
        }
        float[] embedding = ragTransferService.encode(builder.toString());
        doc.add(new KnnFloatVectorField("embedding", embedding, VectorSimilarityFunction.DOT_PRODUCT));
        updateDocument(userId, doc);
    }
    public void deleteUser(long userId) throws IOException {
        writer.deleteDocuments(userTerm(userId));
        writer.commit();
        refreshReader();
    }

    private LuceneSearchResult topDocs2LuceneSearchResult(TopDocs results) throws IOException {
        long[] userIds = new long[results.scoreDocs.length];
        for (int i=0; i < results.scoreDocs.length; i++) {
            Document doc = searcher.storedFields().document(results.scoreDocs[i].doc);
            userIds[i] = Long.valueOf(doc.get("userId"));
        }
        return new LuceneSearchResult(userIds, results.scoreDocs[results.scoreDocs.length-1]);
    }

    public LuceneSearchResult searchByTag(String tag, int count) throws IOException {
        Query query = new TermQuery(new Term("tag", tag));
        TopDocs results = searcher.search(query, count);
        return topDocs2LuceneSearchResult(results);
    }
    public LuceneSearchResult searchByTag(String tag, int count, ScoreDoc lastScoreDoc) throws IOException {
        Query query = new TermQuery(new Term("tag", tag));
        TopDocs results = searcher.searchAfter(lastScoreDoc, query, count);
        return topDocs2LuceneSearchResult(results);
    }

    public LuceneSearchResult searchByEmbedding(float[] embedding, int count) throws IOException {
        Query query = new KnnFloatVectorQuery("embedding", embedding, count);
        TopDocs results = searcher.search(query, count);
        return topDocs2LuceneSearchResult(results);
    }
    public LuceneSearchResult searchByEmbedding(float[] embedding, int count, ScoreDoc lastScoreDoc) throws IOException {
        Query query = new KnnFloatVectorQuery("embedding", embedding, count);
        TopDocs results = searcher.searchAfter(lastScoreDoc, query, count);
        return topDocs2LuceneSearchResult(results);
    }
    public LuceneSearchResult searchExactByTags(String[] tags, int count) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String tag : tags) {
            builder.add(new TermQuery(new Term("tag", tag)), BooleanClause.Occur.MUST);
        }
        Query query = builder.build();
        TopDocs results = searcher.search(query, count);
        return topDocs2LuceneSearchResult(results);
    }
    public LuceneSearchResult searchExactByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String tag : tags) {
            builder.add(new TermQuery(new Term("tag", tag)), BooleanClause.Occur.MUST);
        }
        Query query = builder.build();
        TopDocs results = searcher.searchAfter(lastScoreDoc, query, count);
        return topDocs2LuceneSearchResult(results);
    }
    public LuceneSearchResult searchFuzzyByTags(String[] tags, int count) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String tag : tags) {
            builder.add(new TermQuery(new Term("tag", tag)), BooleanClause.Occur.SHOULD);
        }
        Query query = builder.build();
        TopDocs results = searcher.search(query, count);
        return topDocs2LuceneSearchResult(results);
    }
    public LuceneSearchResult searchFuzzyByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String tag : tags) {
            builder.add(new TermQuery(new Term("tag", tag)), BooleanClause.Occur.SHOULD);
        }
        Query query = builder.build();
        TopDocs results = searcher.searchAfter(lastScoreDoc, query, count);
        return topDocs2LuceneSearchResult(results);
    }

}
