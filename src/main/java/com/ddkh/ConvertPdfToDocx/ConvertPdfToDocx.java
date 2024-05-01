package com.ddkh.ConvertPdfToDocx;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConvertPdfToDocx {
  static String sourceFile = "src/main/resources/template.pdf";
  static String targetFile = "src/main/resources/template.docx";

  public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

    File pdfFile = new File(sourceFile);
    File docxFile = new File(targetFile);

    // Correct the base folder path
    IConverter converter = LocalConverter.builder()
        .baseFolder(new File("src/main/resources"))
        .workerPool(20, 25, 2, TimeUnit.SECONDS)
        .processTimeout(5, TimeUnit.SECONDS)
        .build();

    Future<Boolean> conversion = converter
        .convert(pdfFile).as(DocumentType.PDF)
        .to(docxFile).as(DocumentType.DOCX)
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
