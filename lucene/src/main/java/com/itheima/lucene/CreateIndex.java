package com.itheima.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CreateIndex {

    @Test
    public void createIndex() throws  Exception{
        //创建存储文档的索引库写入对象
        //参数1 为写入的目录对象 表示索引库的创建位置
        Directory directory = FSDirectory.open(new File("D:\\luceneIndex"));
        //参数2 为配置对象 指定lucene的版本和分词器x
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_3);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        IndexWriter writer = new IndexWriter(directory,config);
        //读取本地磁盘文件
        //1.读取本地磁盘文件目录
        File fileDir = new File("D:\\searchsource");
        //2.获取磁盘目录下的所有文件
        File[] files = fileDir.listFiles();
        //3.遍历打印文件的属性 名 内容 大小 路径
        for (File file : files) {
            System.out.println("文件名称为：===="+file.getName());
            System.out.println("文件的内容为：======"+FileUtils.readFileToString(file));
            System.out.println("文件的长度为：======"+file.length());
            System.out.println("文件的路径为：======"+file.getPath());
            //循环过程中 创建document对象
            Document document = new Document();
            //文档中以域field字段存储数据
            /*
             * StringField  只存储 不分词 支持索引查询  存储唯一标识的值
             * TextField    存储   分词   支持索引查询
             * LongField   存储数值  分词 索引查询
             * Store.YES 表示数据创建索引 并且存储
             * Store.NO  表示数据创建索引 不存储数据
             * 使用场景：需要使用的数据都需要存储
             * */
            document.add(new TextField("fileName",file.getName(), Field.Store.YES));
            document.add(new TextField("fileContent",FileUtils.readFileToString(file), Field.Store.YES));
            document.add(new LongField("fileSize",file.length(), Field.Store.YES));
            document.add(new StringField("filePath",file.getPath(), Field.Store.YES));

            //写入创建的文档对象
            writer.addDocument(document);
        }
        //提交索引库的写入
        writer.close();
    }
}
