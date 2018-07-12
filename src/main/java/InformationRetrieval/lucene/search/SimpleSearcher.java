package InformationRetrieval.lucene.search;

import java.io.File;
import java.nio.file.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class SimpleSearcher {

	private static final String indexDirectory = "./indexDirectory/";

	private static final String queryString = "Forget Electro";

	private static final int maxHits = 100;

	public static void main(String[] args) throws Exception {

		File indexDir = new File(indexDirectory);

		SimpleSearcher searcher = new SimpleSearcher();

		searcher.searchIndex(indexDir, queryString);

	}

	private void searchIndex(File indexDir, String queryStr)
			throws Exception {

	    String pathString = indexDir.toString();
        Path path = FileSystems.getDefault()		.getPath(pathString);

		Directory directory = FSDirectory.open(path);
		
		IndexReader  indexReader  = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(indexReader);
		
		Analyzer analyzer = new StandardAnalyzer();
		
		QueryBuilder builder = new QueryBuilder(analyzer);
		
		Query query = builder.createPhraseQuery("content", queryStr);
		
		TopDocs topDocs =searcher.search(query, maxHits);
		
		ScoreDoc[] hits = topDocs.scoreDocs;

		if(hits.length==0){
			System.out.println("No result found for query1, with the content: " + queryStr);
		}else {
			for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println(d.get("fileName") + " Score :" + hits[i].score);

			}
		}
		System.out.println("Found " + hits.length);
		System.out.println("First Query end!");
		System.out.println("----------------------------------------------");
		System.out.println("Second Query start");

		Query query2 = builder.createPhraseQuery("content", queryStr, 4); //Phrase Slop sagt aus wieiviele wörter dazwischen sein dürfen
		TopDocs topDocs2 = searcher.search(query2, maxHits);

		ScoreDoc[] hit2 = topDocs2.scoreDocs;
		if(hit2.length==0){
			System.out.println("No result found for query2, with the content: " + queryStr);
		}else {
			for (int i = 0; i < hit2.length; i++) {
				int docId = hit2[i].doc;
				Document d = searcher.doc(docId);
				System.out.println(d.get("fileName") + " Score :" + hit2[i].score);
			}
		}
		System.out.println("Found " + hit2.length);
        


	}

}
