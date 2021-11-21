package guru.qa.homework.tests;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static guru.qa.homework.helpers.TestData.CREATE_DATE_PDF_FILE;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import guru.qa.homework.helpers.Utils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FilesTest extends BaseTest {

  @Test
  void simpleFileDownloadTest() throws IOException {
    open("https://github.com/selenide/selenide/blob/master/README.md");
    File downloadFile = $("#raw-url").download();
    String fileContent = IOUtils.toString(new FileReader(downloadFile));
    Assertions.assertTrue(
        fileContent.contains("UI Testing Framework powered by Selenium WebDriver"));
  }

  @Test
  void pdfFileDownloadTest() throws IOException {
    open("https://www.xeroxscanners.com/en/en/products/drivers.asp?PN=97-0015-000");
    File downloadFile = $(
        "[href='/downloads/Docs/Certificates/DM152_Section508.EN.pdf']").download();
    PDF pdfParsed = new PDF(downloadFile);
    Assertions.assertEquals(12, pdfParsed.numberOfPages);
    Assertions.assertEquals("John Capurso", pdfParsed.author);
    Assertions.assertEquals(Utils.getTime(CREATE_DATE_PDF_FILE), pdfParsed.creationDate.getTime());
    Assertions.assertEquals("Microsoft Word", pdfParsed.creator);
    Assertions.assertFalse(pdfParsed.encrypted);
    Assertions.assertEquals("", pdfParsed.keywords);
    Assertions.assertEquals("Mac OS X 10.5.8 Quartz PDFContext", pdfParsed.producer);
    Assertions.assertNull(pdfParsed.signatureTime);
    Assertions.assertFalse(pdfParsed.signed);
    Assertions.assertNull(pdfParsed.signerName);
    Assertions.assertEquals("", pdfParsed.subject);
    Assertions.assertEquals("Section 508 VPAT for DM 152 08-31-09", pdfParsed.title);
    String pdfFile = pdfParsed.text;
    Assertions.assertTrue(pdfFile.contains("Section 1194.21 Software Applications and Operating Systems"));
  }

  @Test
  void xlsFileDownloadTest() throws IOException {
    open("https://sbitsnab.ru/prays_list");
    File downloadFile = $("[href='/f/prays_list_sbitsnab.xls']").download();
    XLS xlsParsed = new XLS(downloadFile);
    String cellValue = xlsParsed.excel
        .getSheetAt(0)
        .getRow(9)
        .getCell(2)
        .getStringCellValue();
    Assertions.assertEquals("Доска обрезная 2-сорт", cellValue);
  }

  @Test
  void parseCsvFileTest() throws IOException, CsvException {
    ClassLoader classLoader = this.getClass().getClassLoader();
    try (InputStream inputStream = classLoader.getResourceAsStream("file.csv");
        Reader reader = new InputStreamReader(inputStream)) {
      CSVReader csvReader = new CSVReader(reader);
      List<String[]> allString = csvReader.readAll();
      String[] string = allString.get(1);
      Assertions.assertEquals("Apple", string[0]);
      Assertions.assertEquals("3", string[1]);
    }
  }

  @Test
  void parseZipFileTest() throws IOException {
    ClassLoader classLoader = this.getClass().getClassLoader();
    try (InputStream inputStream = classLoader.getResourceAsStream("file.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
      ZipEntry zipEntry;
      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        Assertions.assertEquals("file.csv", zipEntry.getName());
      }
    }
  }

}
