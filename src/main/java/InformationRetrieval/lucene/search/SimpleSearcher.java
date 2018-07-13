package InformationRetrieval.lucene.search;

import java.io.File;
import java.nio.file.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
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
        Path path = FileSystems.getDefault().getPath(pathString);

		Directory directory = FSDirectory.open(path);
		
		IndexReader  indexReader  = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(indexReader);
		
		Analyzer analyzer = new StandardAnalyzer();
		
		QueryBuilder builder = new QueryBuilder(analyzer);
		
		Query query = builder.createPhraseQuery("content", queryStr); // um nicht nur nach spezifischen Termen zu suchen (Termquery)
		
		TopDocs topDocs =searcher.search(query, maxHits);
		
		ScoreDoc[] hits = topDocs.scoreDocs; // präsentiert einen Treffer, falls einer vorhanden

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

		System.out.println("Second Query end!");
		System.out.println("-----------------------------------------------");
		System.out.println("WildcardQuery starts!");


		Term term = new Term("content", "*lectr*");
		Query wildcardQuery = new WildcardQuery(term);


		TopDocs topDocsWC = searcher.search(wildcardQuery, maxHits);
		ScoreDoc[] hit3wc = topDocsWC.scoreDocs;

		if(hit3wc.length==0){
			System.out.println("No result found for WCquery3, with the wildcard: " + wildcardQuery.toString());
		}else{
			for(int i = 0; i<hit3wc.length; i++){
				int docId = hit3wc[i].doc;
				Document d = searcher.doc(docId);
				System.out.println(d.get("fileName") + " Score: " + hit3wc[i].score);
			}
		}
		System.out.println("Found " + hit3wc.length);

		System.out.println("END WILDCARD!-----------------------------------");
		Query query3 = builder.createBooleanQuery("content", queryStr); // findet alle kombinationen aus den 2 woertern (findet auch electronic)
		TopDocs topDocs3 = searcher.search(query3, maxHits);

		ScoreDoc[] hit3 = topDocs3.scoreDocs;
		if(hit3.length==0){
			System.out.println("No result found for query2, with the content: " + queryStr);
		}else {
			for (int i = 0; i < hit3.length; i++) {
				int docId = hit3[i].doc;
				Document d = searcher.doc(docId);
				System.out.println(d.get("fileName") + " Score :" + hit3[i].score);
			}
		}
		System.out.println("Found " + hit3.length);

		System.out.println("Third Query end!");
		System.out.println("-----------------------------------------------");
		System.out.println("Forth Query starts!");
		System.out.println("----------------------------------------------------");


	}

}
