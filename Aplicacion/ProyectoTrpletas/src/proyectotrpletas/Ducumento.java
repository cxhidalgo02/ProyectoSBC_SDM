/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
URLEncoder.encode(queryText, "UTF-8"),
 */
package proyectotrpletas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFWriterI;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;

/**
 *
 * @author DELL
 */


public class Ducumento {

    public static final String SEPARATOR = ";";
    //public static final String SEPARATOR2 = ";";
    public static final String QUOTE = "\"";

    public Ducumento(int idDocumento, String tituloDocumento, String abstractDocumento, String uriDocumento, String anioDocumento, int numPagesDocumento, String lenguajeDocumento, String doiDocumento, int volumenDocumento, int startDocumento, int endDocumento) {    }
     
    Integer id;
    String titulo;
    String abstrct;
    String uri;
    String anio;
    Integer numPages;
    String lenguaje;
    String doi;
    Integer volumen;
    Integer start;
    Integer end;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        BufferedReader br = null;
        Model model = ModelFactory.createDefaultModel();
        File f = new File("C:\\Users\\DELL\\Desktop\\dataEjemplo.rdf"); //Fijar ruta donde se creará el archivo RDF
        FileOutputStream os = new FileOutputStream(f);

        String dataPrefix = "http://proyecto.org/sbc/data/";
        model.setNsPrefix("myData", dataPrefix);

        //Fijar prefijos de vocabularios incorporados en Jena
        String event = "http://purl.org/NET/c4dm/event.owl/";
        model.setNsPrefix("event", event);
        String c4o = "http://purl.org/spar/c4o/";
        model.setNsPrefix("c4o", c4o);
        String vcard = "http://www.w3.org/2006/vcard/ns/";
        model.setNsPrefix("vcard", vcard);
        String dbo = "http://dbpedia.org/ontology/";
        model.setNsPrefix("dbo", dbo);
        String vivo = "http://vivoweb.org/ontology/";
        model.setNsPrefix("vivo", vivo);
        String bibo = "http://purl.org/ontology/bibo/";
        model.setNsPrefix("bibo", bibo);
        String foaf = "http://xmlns.com/foaf/0.1/";
        model.setNsPrefix("foaf", foaf);
        String  
                dct = "http://purl.org/dc/terms/";
        model.setNsPrefix("dct", dct);
        
        Model dboModel = ModelFactory.createDefaultModel();  // modelo para la ontología
        dboModel.read(dbo);
        
        String auxa = null;
        String[] datos = null;
        
        try {
        
            br = new BufferedReader(new FileReader("C:\\Users\\DELL\\Desktop\\dataEjemplo.csv"));
            String line = br.readLine();
            line = br.readLine();
            System.out.println("--- LEYO Y EXTRAJO DATOS DEL ARCHIVO");
            System.out.print( "LINE  ----- " + line); 
            while (null != line) {
                String[] fields = line.split(SEPARATOR);
                fields = removeTrailingQuotes(fields);
                datos = fields;
                System.out.print( "DATOS ----- " +datos);                              
                String DocumentUri = dataPrefix + String.format(datos[0]);
                /* datos guardados, solo colocar el dato del array */
                /* DOCUMENT */
                String idDoc = datos[0];
                String tituloDoc = datos[1];
                String abstractDoc = datos[2];
                String uriDoc = datos[3];
                String fechaDoc = datos[4];
                String numPagesDoc = datos[5];
                String lenguajeDoc = datos[6];
                String citasDoc = datos[7];
                String numCitasDoc = datos[8];
                /* PERSON */
                String namePer = datos[9];
                String namePer2 = datos[10];
                String namePer3 = datos[11];
                /* ORGANIZATION */
                String tipoDoc = datos[12];
                String nameOrg = datos[13];
                String urlOrg = datos[14];
                
                
                //System.out.println("--- DOCUMENT");
                Resource ducumento = model.createResource(DocumentUri)
                        .addProperty(RDF.type, dboModel.getResource (bibo + "Document"))
                        .addProperty(dboModel.getProperty(bibo, "identifier"), idDoc) //bibo:identifier
                        .addProperty(DCTerms.title, tituloDoc) //dct:title
                        .addProperty(dboModel.getProperty(bibo, "abstract"), abstractDoc) //bibo:abstrac
                        .addProperty(dboModel.getProperty(bibo, "uri"), uriDoc) //bibo:uri 
                        .addProperty(DCTerms.date, fechaDoc) // dct:date
                        .addProperty(dboModel.getProperty(bibo, "numPages"), numPagesDoc) //bibo:doi
                        .addProperty(DCTerms.language, lenguajeDoc) //dce:lenguaje
                        .addProperty(dboModel.getProperty(bibo, "cites"), citasDoc)//bibo:doi
                        .addProperty(dboModel.getProperty(bibo, "numCites"), numCitasDoc); //bibo:doi
                
                //System.out.println("--- PERSON");        
                Resource person = model.createResource(dataPrefix + namePer)        
                        .addProperty(RDF.type, FOAF.Person)
                        .addProperty(DCTerms.creator, dboModel.getResource (bibo + "Document/"+idDoc))
                        .addProperty(FOAF.name, namePer);  
                
                //System.out.println("--- ORGANIZATION");
                Resource organization = model.createResource(dataPrefix + nameOrg)
                        .addProperty(RDF.type, FOAF.Organization)
                        .addProperty(dboModel.getProperty(bibo, "issuer"), (bibo + "Organization/"+idDoc)) //bibo:identifier
                        .addProperty(FOAF.name, nameOrg) //foaf:name
                        .addProperty(FOAF.homepage, urlOrg) //foaf:homePage
                        .addProperty(DCTerms.description, tipoDoc); //DCTemrs.description
                
                line = br.readLine();
            }

        } catch (Exception e) {

        } finally {
            if (null != br) {
                br.close();
            }
        }
        StmtIterator iter = model.listStatements();
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();  // get next statement
            Resource subject = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object = stmt.getObject();      // get the object
            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
        }
        System.out.println("MODELO RDF------");
        model.write(System.out, "N-TRIPLE");
        // Save to a file
        RDFWriterI writer = model.getWriter("N-TRIPLE"); //RDF/XML - N-TRIPLE
        writer.write(model, os, "");
        
        dboModel.close();
        model.close();
        
        
    }
    
    /*nuevoDoc.setId(idDocumento);
                nuevoDoc.setTitulo(tituloDocumento);
                nuevoDoc.setAbstrct(abstractDocumento);
                nuevoDoc.setUri(uriDocumento);
                nuevoDoc.setAnio(anioDocumento);
                nuevoDoc.setNumPages(numPagesDocumento);
                nuevoDoc.setLenguaje(lenguajeDocumento);
                nuevoDoc.setDoi(doiDocumento);
                nuevoDoc.setVolumen(volumenDocumento);
                nuevoDoc.setStart(startDocumento);
                nuevoDoc.setEnd(endDocumento);*/
                
               /* int ID = nuevoDoc.getId();
                String Titulo = nuevoDoc.getTitulo();
                String Abstract = nuevoDoc.getAbstrct();
                //String Uri = nuevoDoc.getUri();
                String Anio = nuevoDoc.getAnio();
                int NumeroPag = nuevoDoc.getNumPages();
                String Lenguaje = nuevoDoc.getLenguaje();
                String Doi = nuevoDoc.getDoi();
                int Volumen = nuevoDoc.getVolumen();
                int PagInicio = nuevoDoc.getStart();
                int PagFin = nuevoDoc.getEnd();*/

        public int getId() {
           return id;
        }
        public String getTitulo() {
           return titulo;
        }
        public String getAbstrct() {
           return abstrct;
        }
        public String getUri() {
           return uri;
        }
        public String getAnio() {
           return anio;
        }
        public Integer getNumPages() {
           return numPages;
        }
        public String getLenguaje() {
           return lenguaje;
        }
        public String getDoi() {
           return doi;
        }
        public Integer getVolumen() {
           return volumen;
        }
        public Integer getStart() {
           return start;
        }
        public Integer getEnd() {
           return end;
        }
        
        public void setId(Integer id) {
           this.id = id;
        }
        public void setTitulo(String titulo) {
           this.titulo = titulo;
        }
        public void setAbstrct(String abstrct) {
           this.abstrct = abstrct;
        }
        public void setUri(String uri) {
           this.uri = uri;
        }
        public void setAnio(String anio) {
           this.anio = anio;
        }
        public void setNumPages(Integer numPages) {
           this.numPages = numPages;
        }
        public void setLenguaje(String lenguaje) {
           this.lenguaje = lenguaje;
        }
        public void setDoi(String doi) {
           this.doi = doi;
        }
        public void setVolumen(Integer volumen) {
           this.volumen = volumen;
        }
        public void setStart(Integer start) {
           this.start = start;
        }
        public void setEnd(Integer end) {
           this.end = end;
        }
        
    private static String[] removeTrailingQuotes(String[] fields) {

        String result[] = new String[fields.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = fields[i].replaceAll("^" + QUOTE, "").replaceAll(QUOTE + "$", "");
        }
        return result;
    }
}
