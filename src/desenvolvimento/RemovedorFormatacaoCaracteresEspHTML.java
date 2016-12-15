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
		String pastaOrigem = "tagsReduzidas";
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
		
		//Regex principais
		String removerTagAbertura = "<(a( href=\"[A-Za-z0-9:?/%]+\")?|b|p|span|strong|br|form|body|head|div)>";
		String removerTagFim = "</(a|b|p|span|strong|br|form|body|head|div)>";
		
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
				decreto = decreto.replaceAll("&sup2;","\u00B2");
				decreto = decreto.replaceAll("&sup3;","\u00B3");
				decreto = decreto.replaceAll("&sect;","\u00A7");
				
				decreto = decreto.replaceAll(removerTagAbertura, "");
				decreto = decreto.replaceAll(removerTagFim, "");
				
					
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
				
				System.out.println(i + "   " + j + " foi limpo");
				
			}
		}
	}
}