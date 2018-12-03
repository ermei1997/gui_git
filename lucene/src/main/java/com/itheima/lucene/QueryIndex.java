package com.itheima.lucene;



import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.File;
import java.io.IOException;

public class QueryIndex {

    //查询所有
    @Test
    public void queryAll() throws Exception {

        Query query = new MatchAllDocsQuery();
        doQuery(query);
    }

    //字条查询
    @Test
    public void queryByTerm()throws Exception {
        Query query = new TermQuery(new Term("fileName","传智播客"));
        doQuery(query);
    }

    //范围查询 文件大小1---50之间
    @Test
    public void queryByRange()throws IOException {
        Query query = NumericRangeQuery.newLongRange("fileSize",1L,50L,true,true);
        doQuery(query);
    }

    //单域字段查询
    @Test
    public void queryByParser()throws Exception {
        //定义查询的关键短语
        String queryStr="传智播客是一个不明觉厉的公司";
        //创建分词解析对象
        QueryParser parser = new QueryParser("fileName",new IKAnalyzer());
        //使用分词解析对象对字符串解析得到query对象
        Query query = parser.parse(queryStr);
        doQuery(query);
    }

    //多域字段查询
    @Test
    public void queryByMultiParser()throws Exception {
        //定义查询的关键短语
        String queryStr="传智播客是一个不明觉厉的公司";
        //创建多个域字段的数组
        String [] fileds = {"fileName","fileContent"};
        //创建分词解析对象
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fileds,new IKAnalyzer());
        //使用分词解析对象对字符串解析得到query对象
        Query query = parser.parse(queryStr);
        doQuery(query);
    }

    private void doQuery(Query query) throws IOException {
        //创建索引库的读取对象
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("D:\\luceneIndex")));
        //创建用于搜索索引库的对象
        IndexSearcher searcher = new IndexSearcher(reader);
        //参数1 query表示 查询对象
        //参数2 表示查询结果条数
        TopDocs topDocs = searcher.search(query, 100);
        //查询结果命中总数量
        System.out.println("文档命中总数量为：=====" + topDocs.totalHits);
        //每个文档id和得分的数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //循环遍历得到文档id 和得分
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的id为：=======" + scoreDoc.doc);
            System.out.println("当前文档得分为：====" + scoreDoc.score);
            //通过当前文档的id获取
            Document document = searcher.doc(scoreDoc.doc);
            //获取文档的域数据
            System.out.println("文档名称为：=====" + document.get("fileName"));
            System.out.println("文档内容为：=====" + document.get("fileContent"));
            System.out.println("文档大小为：=====" + document.get("fileSize"));
            System.out.println("文档路径为：=====" + document.get("filePath"));
        }
    }



}
