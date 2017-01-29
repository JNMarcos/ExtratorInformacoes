/**
 * 
 */
package desenvolvimento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author JN
 *
 */
public class RemovedorFormatacaoCaracteresEspHTML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String pastaDestino = "semFormata��o";
		String pastaOrigem = "tagsEliminadas";
		String nomeArquivo;
		String decreto;
		String linha;
		File arquivoPasta;

		String[] caminhosDecretos = new String[3]; 
		caminhosDecretos[0] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2014\\";
		caminhosDecretos[1] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2015\\";
		caminhosDecretos[2] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2016\\";

		//"Cria-se" os Files que apontam para as pastas dos decretos
		File[] pastas = new File[3];
		pastas[0] = new File(caminhosDecretos[0] + pastaOrigem);
		pastas[1] = new File(caminhosDecretos[1] + pastaOrigem);
		pastas[2] = new File(caminhosDecretos[2] + pastaOrigem);

		//Usado para conter a lista de endere�os dos arquivos dentro de cada pasta
		File[][] arquivos = new File[3][];

		for (int  i = 0; i < pastas.length; i++){
			arquivos[i] = pastas[i].listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			});
			System.out.println(arquivos[i]);
		}

		//Buffer
		FileReader fr;
		BufferedReader br;
		FileWriter fw;
		BufferedWriter bw;

		Pattern padrao;
		Matcher matcher;

		String substituirPor;
		//AQUI COME�A A REMO��O DA FORMATA��O
		for (int i = 0; i < caminhosDecretos.length; i++){
			arquivoPasta = new File(caminhosDecretos[i] + pastaDestino);
			System.out.println(arquivoPasta);
			if (!arquivoPasta.exists()){ //Se pasta n�o existe, cria
				arquivoPasta.mkdir();
			}
			for (int j = 0; j < arquivos[i].length; j++){
				nomeArquivo = (arquivos[i][j]).getName(); //obt�m o nome do arquivo
				decreto = "";
				//Arquivo de leitura a partir do caminhoTemporario (string)
				try {
					fr = new FileReader(arquivos[i][j].getPath());
					//Cria-se o buffer para ler do arquivo
					br = new BufferedReader(fr);

					linha = br.readLine();

					while(linha != null){
						decreto = decreto + " " + linha;
						linha = br.readLine();
					}

					br.close();
					fr.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//substitui��o de s�mbolos (letras)
				decreto = decreto.replaceAll("�", "");
				decreto = decreto.replaceAll("&aacute;", "�");
				decreto = decreto.replaceAll("&eacute;", "�");
				decreto = decreto.replaceAll("&iacute;", "�");
				decreto = decreto.replaceAll("&oacute;", "�");
				decreto = decreto.replaceAll("&uacute;", "�");
				decreto = decreto.replaceAll("&Aacute;", "�");
				decreto = decreto.replaceAll("&Eacute;", "�");
				decreto = decreto.replaceAll("&Iacute;", "�");
				decreto = decreto.replaceAll("&Oacute;", "�");
				decreto = decreto.replaceAll("&Uacute;", "�");
				decreto = decreto.replaceAll("&acirc;", "�");
				decreto = decreto.replaceAll("&ecirc;", "�");
				decreto = decreto.replaceAll("&ocirc;", "�");
				decreto = decreto.replaceAll("&Acirc;", "�");
				decreto = decreto.replaceAll("&Ecirc;", "�");
				decreto = decreto.replaceAll("&Ocirc;", "�");
				decreto = decreto.replaceAll("&agrave;", "�");
				decreto = decreto.replaceAll("&Agrave;", "�");
				decreto = decreto.replaceAll("&atilde;", "�");
				decreto = decreto.replaceAll("&otilde;", "�");
				decreto = decreto.replaceAll("&Atilde;", "�");
				decreto = decreto.replaceAll("&Otilde;", "�");
				decreto = decreto.replaceAll("&ccedil;", "�");
				decreto = decreto.replaceAll("&Ccedil;", "�");
				decreto = decreto.replaceAll("&uuml;", "u");//u com trema
				decreto = decreto.replaceAll("&Uuml;;", "U");

				//substitui��o de s�mbolos (sinais)
				decreto = decreto.replaceAll("&nbsp;", " ");
				decreto = decreto.replaceAll("&quot;", "\"");
				decreto = decreto.replaceAll("&ldquo;", "\"");
				decreto = decreto.replaceAll("&rdquo;", "\"");
				decreto = decreto.replaceAll("&lsquo;", "'");
				decreto = decreto.replaceAll("&rsquo;", "'");
				decreto = decreto.replaceAll("&ordm;", "�");
				decreto = decreto.replaceAll("&ordf;", "�");
				decreto = decreto.replaceAll("&deg;", "�");
				decreto = decreto.replaceAll("&ndash;", "-");
				decreto = decreto.replaceAll("&mdash;", "-");
				decreto = decreto.replaceAll("&sect;","�");
				decreto = decreto.replaceAll("\u2033", "\"");
				decreto = decreto.replaceAll("&acute; ", "\'");
				decreto = decreto.replaceAll("&amp;", "&");
				decreto = decreto.replaceAll("&hellip;", ".");
				decreto = decreto.replaceAll("\\Q�?\\E", "fi");
				decreto = decreto.replaceAll("�", " ");


				//conserta a numera��o do decreto
				
				padrao = Pattern.compile("(\\d{1,3})\\s*\\.\\s+(\\d{3})[^�]");
				matcher = padrao.matcher(decreto);
				if (matcher.find()){
					//System.out.println(nomeArquivo + "  numeracaoDecreto" + "   "+ matcher.group(1) + matcher.group(2));
					substituirPor = matcher.group(1) + "." + matcher.group(2);
					decreto = decreto.replaceFirst("\\d{1,3}\\s*\\.\\s+(\\d{3})[^�]", substituirPor);
				}

				//remove interroga��o quando estiver entre letras. ? proveniente de erro durante a convers�o anterior
				padrao = Pattern.compile("(\\w)\\Q?\\E(\\w)");
				matcher = padrao.matcher(decreto);

				if (matcher.find()){
					//System.out.println(nomeArquivo + "  ?");
					substituirPor = matcher.group(1) + matcher.group(2);
					decreto = decreto.replaceFirst("\\w\\Q?\\E\\w", substituirPor);	
				}
				String[] partes = decreto.split("Pal�cio do Campo das Princesas");

				padrao = Pattern.compile("( ){2,}(\\w)");
				
				for (int k = 0; k < partes.length && k != 1; k++){
					matcher = padrao.matcher(partes[k]);
					while (matcher.find()){
						//System.out.println(nomeArquivo + "  espa�o letra");
						substituirPor = " " + matcher.group(2);
						partes[k] = partes[k].replaceFirst("( ){2,}(\\w)", substituirPor);
					}
				}

				decreto = partes[0];
				for (int k = 1; k < partes.length; k++){
					decreto += "Pal�cio do Campo das Princesas" + partes[k]; 
				}

				
				//ano separado corre��o
				padrao = Pattern.compile(" de (\\d{1,3})\\s+(\\d{1,3})");
				matcher = padrao.matcher(decreto);
				while (matcher.find()){
					substituirPor = " de " + matcher.group(1) + matcher.group(2);
					decreto = decreto.replaceFirst(" de (\\d{1,3})\\s+(\\d{1,3})", substituirPor);	
				}
				
				padrao = Pattern.compile("R\\$(\\d{1,3})");
				matcher = padrao.matcher(decreto);
				while (matcher.find()){
					substituirPor = "R\\$ " + matcher.group(1);
					decreto = decreto.replaceFirst("R\\$(\\d{1,3})", substituirPor);	
				}
				
				padrao = Pattern.compile("(\\d+)(mm|cm|dm|hm|km|m|ha)( |;|:|,|e|E|N|O|L|S|\\Q(\\E|\\.)");
				matcher = padrao.matcher(decreto);
				while (matcher.find()){
					substituirPor = matcher.group(1) + " " + matcher.group(2) + (matcher.group(3).matches("[ENOLSe]")? " " + matcher.group(3): matcher.group(3));
					decreto = decreto.replaceFirst("(\\d+)(mm|cm|dm|hm|km|m|ha)( |;|:|,|e|E|N|O|L|S|\\Q(\\E|\\.)", substituirPor);	
				}
				
				/*
				padrao = Pattern.compile(":([0-9A-Za-z����������������������])");
				matcher = padrao.matcher(decreto);
				while (matcher.find()){
					substituirPor = ": " + matcher.group(1);
					decreto = decreto.replaceFirst(":([0-9A-Za-z����������������������])", substituirPor);	
				}
*/
				//corre��o de formata��o
				decreto = decreto.replaceFirst("\\s{2,}DE", " DE");	
				decreto = decreto.replaceAll("( )+,", ",");
				decreto = decreto.replaceAll("( )+\\.", ".");
				decreto = decreto.replaceAll("( )+;", ";");
				decreto = decreto.trim();

				//corre��o de palavras
				decreto = decreto.replaceAll("A( )+tiva", "Ativa");
				decreto = decreto.replaceAll("CON S I D E R AN DO", "CONSIDERANDO");
				decreto = decreto.replaceAll(" ONSIDERANDO", " CONSIDERANDO");
				decreto = decreto.replaceAll("GVERNO|GPVERNO", "GOVERNO");
				decreto = decreto.replaceAll("GVERNADOR", "GOVERNADOR");
				decreto = decreto.replaceAll("MEORIAL", "MEMORIAL");
				decreto = decreto.replaceAll("daLei", "da Lei");
				decreto = decreto.replaceAll("O G O VER N ADOR DO E S T ADO", "O GOVERNADOR DO ESTADO");
				decreto = decreto.replaceAll("D\\s*E\\s*C\\s*R\\s*E\\s*T\\s*A", "DECRETA");
				decreto = decreto.replaceAll("SETMBRO", "SETEMBRO");


				try {
					fw = new FileWriter(new File(arquivoPasta + "\\\\" + nomeArquivo));
					bw = new BufferedWriter(fw);
					bw.write(decreto);
					bw.flush();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println(nomeArquivo + " foi limpo");

			}
		}
	}
}