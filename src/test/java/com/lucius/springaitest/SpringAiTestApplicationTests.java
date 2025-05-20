package com.lucius.springaitest;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.List;

@SpringBootTest
class SpringAiTestApplicationTests {
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Test
    public void testVectorStore() {
        Resource resource = new FileSystemResource("C:\\Users\\86180\\Desktop\\4 计算机网络名词解释 (2).pdf");
        // 1. 创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每页PDF作为一个Document
                        .build()
        );
        // 2. 读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 3. 写入向量库
        vectorStore.add(documents);
        // 4. 搜索
        vectorStore.similaritySearch("迭代查询是什么");
        for (Document document : documents) {
            System.out.println(document.getId());
            System.out.println(document.getScore());
            System.out.println(document.getText());
        }
    }

}
