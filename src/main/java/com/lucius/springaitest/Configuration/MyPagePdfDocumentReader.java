package com.lucius.springaitest.Configuration;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyPagePdfDocumentReader {
    @Autowired
    private VectorStore vectorStore;

    private void writeFileToVectorStore(Resource resource) {
        // 1. 创建PagePdfDocumentReader
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2. 读取PDF文件为Document格式
        List<Document> documents = reader.read();
        // 3. 将文档写入向量数据库
        vectorStore.add(documents);
    }

}