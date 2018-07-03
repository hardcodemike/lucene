package InformationRetrieval.lucene.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class SimpleIndexer {

	private static final String indexDirectory = "./data";

	private static final String dirToBeIndexed = "./data";

	public static void main(String[] args) throws Exception {

		File indexDir = new File(indexDirectory);

		File dataDir = new File(dirToBeIndexed);

		SimpleIndexer indexer = new SimpleIndexer();

		int numIndexed = indexer.index(indexDir, dataDir);

		System.out.println("Total files indexed " + numIndexed);
	}

	private int index(File indexDir, File dataDir) throws IOException {

		Analyzer analyzer = new StandardAnalyzer();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		String pathString = indexDir.toString();
		Path path = FileSystems.getDefault().getPath(pathString);
		IndexWriter indexWriter = new IndexWriter(FSDirectory.open(path),
				config);

		indexWriter.deleteAll();

		File[] files = dataDir.listFiles();

		for (File f : files) {
			System.out.println("Indexing file " + f.getCanonicalPath());

			Document doc = new Document();

			doc.add(new TextField("content", new FileReader(f)));

			doc.add(new StoredField("fileName", f.getCanonicalPath()));

			indexWriter.addDocument(doc);
		}

		int numIndexed = indexWriter.maxDoc();

		indexWriter.close();

		return numIndexed;

	}

}
