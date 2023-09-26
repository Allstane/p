package world.library.custom

import cats.effect.IO
import com.lowagie.text.{Cell, Chunk, Document, Element, Font, Paragraph, Phrase, Table}
import com.lowagie.text.pdf.PdfWriter
import world.library.data.{Book, BookF, Chapter}
import java.awt.Color
import java.io.FileOutputStream


object Openpdf {

  def putPdfMetadata(doc: Document, book: Book): Boolean = {
    doc.addTitle(s"Alefowl.com - ${book.author} - ${book.title}")
    doc.addSubject("Book is created by Alefowl.com")
    doc.addKeywords("Alefowl, book")
    doc.addCreator("Alefowl.com")
    doc.addAuthor(book.author)
  }

  def createPdfSingleBook(bookF: BookF): Unit = {

    def insertChapter(doc: Document, chapter: Chapter, titleFont: Font): Boolean = {
      val paragraph1 = new Paragraph(chapter.title, titleFont)
      val paragraph2 = new Paragraph(chapter.txt.getOrElse(""))
      paragraph1.setAlignment(Element.ALIGN_JUSTIFIED)
      doc.add(paragraph1)
      doc.add(paragraph2)
      doc.add(new Paragraph(Chunk.NEWLINE))
    }

    val book = bookF.book
    val chapters = bookF.chapters

    val myPDFDoc = new Document()

    val pdfOutputFile = new FileOutputStream(s"/Users/kapinuss/IdeaProjects/lib-view/books/${book.id}.pdf")
    //val baos = new ByteArrayOutputStream();

    val pdfWriter: PdfWriter = PdfWriter.getInstance(myPDFDoc, pdfOutputFile)

    myPDFDoc.open()

    putPdfMetadata(myPDFDoc, book)

    val titleFont = new Font(Font.COURIER, 20f, Font.BOLDITALIC, Color.BLUE)

    val paragraph1 = new Paragraph(book.author, titleFont)
    val paragraph2 = new Paragraph(book.title, titleFont)
    paragraph1.setAlignment(Element.ALIGN_CENTER)
    paragraph2.setAlignment(Element.ALIGN_CENTER)

    myPDFDoc.add(paragraph1)
    myPDFDoc.add(paragraph2)
    myPDFDoc.add(new Paragraph(Chunk.NEWLINE))

    chapters.foreach(chapter => insertChapter(myPDFDoc, chapter, titleFont))

    myPDFDoc.close()

    pdfWriter.close()
  }

  def createPdfDoubleBook(b1: BookF, b2: BookF): IO[Unit] = IO {

    def insertChapters(table: Table, chapter1: Chapter, chapter2: Chapter): Unit = {
      table.addCell(new Cell(new Phrase(s"${chapter1.title}") ))
      table.addCell(new Cell(new Phrase(s"${chapter2.title}") ))
      table.addCell(new Cell(new Phrase(s"${chapter1.txt.getOrElse("")}") ))
      table.addCell(new Cell(new Phrase(s"${chapter2.txt.getOrElse("")}")))
    }

    val book: Book = b1.book
    val chapters: List[Chapter] = b1.chapters

    val book2: Book = b2.book
    val chapters2 = b2.chapters

    val myPDFDoc = new Document()

    putPdfMetadata(myPDFDoc, book)

    val pdfOutputFile = new FileOutputStream(s"/Users/kapinuss/IdeaProjects/lib-view/books/${book.id}.pdf")

    val pdfWriter = PdfWriter.getInstance(myPDFDoc, pdfOutputFile)

    myPDFDoc.open()

    val titleFont = new Font(Font.COURIER, 20f, Font.BOLDITALIC, Color.BLUE)

    val paragraph1 = new Paragraph(book.author, titleFont)
    val paragraph2 = new Paragraph(book.title, titleFont)
    paragraph1.setAlignment(Element.ALIGN_CENTER)
    paragraph2.setAlignment(Element.ALIGN_CENTER)

    val myTable = new Table(2)
    myTable.setPadding(2f)
    myTable.setBorder(0)
    myTable.setSpacing(1f)
    myTable.setWidth(100f)

    val headerTable = List(book.title, book2.title)

    headerTable.foreach(e => {
      val current = new Cell(new Phrase(e))
      current.setHeader(true)
      current.setBackgroundColor(Color.WHITE)
      myTable.addCell(current)
    })

    chapters.indices.foreach(i => insertChapters(myTable, chapters(i), chapters2(i) ))

    myPDFDoc.add(paragraph1)
    myPDFDoc.add(paragraph2)
    myPDFDoc.add(new Paragraph(Chunk.NEWLINE))
    myPDFDoc.add(myTable)

    myPDFDoc.close()
    pdfWriter.close()
  }
}
