package com.itheima.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class UpdateIndex {

    @Test
    public void updateIndex() throws  Exception{
        //创建存储文档的索引库写入对象
        //参数1 为写入的目录对象 表示索引库的创建位置
        Directory directory = FSDirectory.open(new File("D:\\luceneIndex"));
        //参数2 为配置对象 指定lucene的版本和分词器x
        // Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        IndexWriter writer = new IndexWriter(directory,config);

        //参数1表示根据term修改文档
        //参数2 表示修改后的文档数据
        //修改逻辑为：先通过term移除匹配的数据 添加新的数据
        //建议修改数据 通过唯一标识的域字段查询修改
        Document document = new Document();
        document.add(new TextField("fileName","测试修改", Field.Store.YES));
        writer.updateDocument(new Term("fileName","spring"),document);


        //提交索引库的写入
        writer.close();
    }

    @Test
    public void deleteIndex() throws  Exception{
        //创建存储文档的索引库写入对象
        //参数1 为写入的目录对象 表示索引库的创建位置
        Directory directory = FSDirectory.open(new File("D:\\luceneIndex"));
        //参数2 为配置对象 指定lucene的版本和分词器x
        // Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        IndexWriter writer = new IndexWriter(directory,config);
        //删除匹配字条的数据
        writer.deleteDocuments(new Term("fileName","测试"));
        writer.deleteAll();
        //提交索引库的写入
        writer.close();
    }

}
