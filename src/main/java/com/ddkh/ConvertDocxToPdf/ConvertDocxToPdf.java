package com.ddkh.ConvertDocxToPdf;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ConvertDocxToPdf {

  static String sourceFile = "src/main/resources/template.docx";
  static String targetFile = "src/main/resources/template.pdf";

  public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

    File docxFile = new File(sourceFile);
    File pdfFile = new File(targetFile);

    // Correct the base folder path
    IConverter converter = LocalConverter.builder()
        .baseFolder(new File("src/main/resources"))
        .workerPool(20, 25, 2, TimeUnit.SECONDS)
        .processTimeout(5, TimeUnit.SECONDS)
        .build();

    Future<Boolean> conversion = converter
        .convert(docxFile).as(DocumentType.DOCX)
        .to(pdfFile).as(DocumentType.PDF)
        .schedule();

    try {
      conversion.get();
      System.out.println("Conversion completed successfully!");
    } catch (ExecutionException e) {
      System.err.println("Conversion failed: " + e.getMessage());
    } finally {
      // Close the converter
      converter.shutDown();
    }
  }
}
