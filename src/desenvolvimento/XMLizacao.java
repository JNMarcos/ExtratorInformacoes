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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JN
 *
 */
public class XMLizacao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Pastas de origem e destino
		String pastaDestino = "XMLizada";
		String pastaOrigem = "semFormataηγo";

		//Caminhos dos decretos
		String[] caminhosDecretos = new String[3]; 
		caminhosDecretos[0] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2014\\";
		caminhosDecretos[1] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2015\\";
		caminhosDecretos[2] = "C:\\Users\\JN\\Documents\\DecretosAlepe\\2016\\";

		//"Cria-se" os Files que apontam para as pastas dos decretos
		File[] pastas = new File[3];
		pastas[0] = new File(caminhosDecretos[0] + pastaOrigem);
		pastas[1] = new File(caminhosDecretos[1] + pastaOrigem);
		pastas[2] = new File(caminhosDecretos[2] + pastaOrigem);

		//Usado para conter a lista de endereηos dos arquivos dentro de cada pasta
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

		//
		String nomeArquivo;
		File arquivoPasta;
		Map<String, Integer> contadorTiposArquivos = new Hashtable<>();
		contadorTiposArquivos.put("Abre", 0);
		contadorTiposArquivos.put("Aloca", 0);
		contadorTiposArquivos.put("Altera", 0);
		contadorTiposArquivos.put("Aprova", 0);
		contadorTiposArquivos.put("Autoriza", 0);
		contadorTiposArquivos.put("Concede", 0);
		contadorTiposArquivos.put("Cria", 0);
		contadorTiposArquivos.put("Declara", 0);
		contadorTiposArquivos.put("Dispυe", 0);
		contadorTiposArquivos.put("Estabelece", 0);
		contadorTiposArquivos.put("Institui", 0);
		contadorTiposArquivos.put("Introduz", 0);
		contadorTiposArquivos.put("Qualifica", 0);
		contadorTiposArquivos.put("Redenomina", 0);
		contadorTiposArquivos.put("Regulamenta", 0);
		contadorTiposArquivos.put("Revoga", 0);
		contadorTiposArquivos.put("Transfere", 0);
		contadorTiposArquivos.put("Transforma", 0);

		//Regex
		
		String regexTiposDecretos = "<\\w{1,6}>(Abre|Aloca|Altera|Aprova|Autoriza|Concede|Cria|Declara|Dispυe|Estabelece|Institui|Introduz|Qualifica|Redenomina|Regulamenta|Revoga|Transfere|Transforma)([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%0-9Ί°, ]+)\\s*(<a( href=\"[A-Za-z0-9?/\\%:]+\")?>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%0-9Ί°,/ ]+)\\s*</a>(,)?([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%0-9Ί°,/ ]+))?\\s*</\\w{1,6}>";
		String regexTiposDecretos2 = "<\\w{1,6}>\\s*<\\w{1,6}>(Abre|Aloca|Altera|Aprova|Autoriza|Concede|Cria|Declara|Dispυe|Estabelece|Institui|Introduz|Redenomina|Regulamenta|Revoga|Transfere|Transforma)([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%0-9Ί°,/ \u00A7\u002E\u002D\u2014]+)</\\w{1,6}>\\s*<\\w{1,6}>(\\s*<a href=\"[A-Za-z0-9?/\\%:]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%0-9Ί°,/ \u00A7\u002E\u002D\u2014]+)\\s*</a>(,)?([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%0-9Ί°,/ \u00A7\u002E\u002D\u2014]+))?\\s*</\\w{1,6}>\\s*<\\w{1,6}>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%0-9Ί°,/ \u00A7\u002E\u002D\u2014]+)";
		String regexTiposDecretos3 = "<\\w{1,6}>(Abre|Aloca|Altera|Aprova|Autoriza|Concede|Cria|Declara|Dispυe|Estabelece|Institui|Introduz|Qualifica|Redenomina|Regulamenta|Revoga|Transfere|Transforma)([0-9A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?$%Ί°, -]+)\\s*</\\w{1,6}>";
		String padraoDecreto = "DECRETO\\s*.*\\s*(</b>)?\\s*(<b>)?(\\d{2,3})\\s*\\.\\s*(\\d{3})\\s*,\\s*DE\\s*(\\d{1,2})\\s*DE\\s*([A-Za-z&Ηη;]*)\\s*DE\\s*(\\d{4}";
		String padraoOGovernador = "(O |<span>O</span>\\s*<span>)?(GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|O PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERCΝCIO DO CARGO DE GOVERNADOR DO ESTADO)\\s*(</span>)?\\s*(<span>)?([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)\\s*(</span>)?\\s*(<a href=\"[A-Za-z0-9?/]+\">)?\\s*(<span>)?([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]*)(</span>)?\\s*(</a>)?(,)?\\s*((</span>)\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)\\s*(<span>)([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]*)\\s*(</span>))*(<span>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)</span>)*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)*\\s*(<a href=\"[A-Za-z0-9?/]+\">\\s*(<span>)?([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7]+))*(</a>,([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)\\s*<a href=\"[A-Za-z0-9?/]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)</a>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)\\s*<a href=\"[A-Za-z0-9?/]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)</a>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)\\s*<a href=\"[A-Za-z0-9?/]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)</a>(,)?)*";
		String padraoOGovernador2= "(O |<span>O</span>\\s*<span>)?(GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|O PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERCΝCIO DO CARGO DE GOVERNADOR DO ESTADO)\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7]+)\\s*<a href=\"[A-Za-z0-9?/]+\">\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]*)\\s*</a>\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]*)\\s*<a href=\"[A-Za-z0-9?/]+\">\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]*)\\s*</a>\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]*)\\s*(<a href=\"[A-Za-z0-9?/]+\">\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]*)\\s*</a>\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]*))*\\s*(<a href=\"[A-Za-z0-9?/]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ\"!?0-9Ί°,/ \u002D\u002E\u00A7]*)</a>)*(,)?";
		String padraoOGovernador3 = "<span>(O GOVERNADOR DO ESTADO,) ([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ ]+)*\\s*</span>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)*\\s*<span>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°, \u002D\u002E\u00A7\u2014]+)*</span>\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)*\\s*<a href=\"[A-Za-z0-9?/]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)*</a>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)\\s*<a href=\"[A-Za-z0-9?/]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)*</a>\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)*\\s*<span>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)*\\s*<a href=\"[A-Za-z0-9?/]+\">([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)</a>([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)\\s*</span>\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)\\s*([A-Za-zηγΰαβικνστυϊΒΓΑΐΙΚΝΣΤΥΪΗ=\"!?0-9Ί°,/ \u002D\u002E\u00A7\u2014]+)";

		//Padrυes e Matchers
		Pattern padrao1;
		Pattern padrao2;
		Pattern padrao3;
		Pattern padrao4;
		Pattern padrao5;
		Pattern padrao6;
		Matcher matcher1;
		Matcher matcher2;
		Matcher matcher3;
		Matcher matcher4;
		Matcher matcher5;

		String dia, mes, ano;
		String decretoFinal = "";
		String decretoIntermediario = "";
		String parteDecreto = "";
		String decreto;
		String linha;
		//AQUI COMEΗA A REMOΗΓO DA FORMATAΗΓO
		for (int i = 0; i < caminhosDecretos.length; i++){
			arquivoPasta = new File(caminhosDecretos[i] + pastaDestino);
			//System.out.println(arquivoPasta);
			if (!arquivoPasta.exists()){ //Se pasta nγo existe, cria
				arquivoPasta.mkdir();
			}
			padrao1 = Pattern.compile(padraoOGovernador);
			padrao2 = Pattern.compile(padraoOGovernador2);
			padrao3 = Pattern.compile(padraoOGovernador3);
			padrao4 = Pattern.compile(regexTiposDecretos);
			padrao5 = Pattern.compile(regexTiposDecretos2);
			padrao6 = Pattern.compile(regexTiposDecretos3);
			
			for (int j = 0; j < arquivos[i].length; j++){
				nomeArquivo = (arquivos[i][j]).getName(); //obtιm o nome do arquivo
				//System.out.println(nomeArquivo);
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

					//decreto = decreto.replaceAll("\u00C2\u00A0", " ");
					for (int etapa = 0; etapa < 1; etapa++){
						decretoIntermediario = decreto;
						//matcher1 = padrao1.matcher(decretoIntermediario);
						//matcher2 = padrao2.matcher(decretoIntermediario);
						//matcher3 = padrao3.matcher(decretoIntermediario);
						matcher4 = padrao4.matcher(decretoIntermediario);
						matcher3 = padrao5.matcher(decretoIntermediario);
						matcher5 = padrao6.matcher(decretoIntermediario);

						//Se der match e nγo tiver entrado no if ainda, entre
						if (matcher5.find()){
							parteDecreto = matcher5.group(1) + matcher5.group(2);
						System.out.println("entrou aqui PORRRRRRRRRS");
						}
						else if (matcher3.find()){
							parteDecreto = matcher3.group(1) + matcher3.group(2);
							if (matcher3.group(4) != null) parteDecreto = parteDecreto + matcher3.group(4);
							if (matcher3.group(5) != null) parteDecreto = parteDecreto + matcher3.group(5);
							if (matcher3.group(6) != null) parteDecreto = parteDecreto + matcher3.group(6);
							parteDecreto = parteDecreto + matcher3.group(7);
							contadorTiposArquivos.replace(matcher3.group(1), 
									contadorTiposArquivos.get(matcher3.group(1)) + 1);
							System.out.println(matcher3.group(1) + " " + contadorTiposArquivos.get(matcher3.group(1)));	
						} else if (matcher4.find()){
							parteDecreto = matcher4.group(1) + matcher4.group(2);
							if (matcher4.group(5) != null) parteDecreto = parteDecreto + matcher4.group(5);
							if (matcher4.group(6) != null) parteDecreto = parteDecreto + matcher4.group(6);
							if (matcher4.group(7) != null) parteDecreto = parteDecreto + matcher4.group(7);

							contadorTiposArquivos.replace(matcher4.group(1), 
									contadorTiposArquivos.get(matcher4.group(1)) + 1);

							System.out.println(matcher4.group(1) + " " + contadorTiposArquivos.get(matcher4.group(1)));

						} 
						else
						{System.out.println(decretoIntermediario.substring(50, 500));}
						/*
						else if (matcher3.find() && etapa == 1){
							System.out.println("allll");
							parteDecreto = matcher3.group(1) + matcher3.group(2) +
									matcher3.group(3) + matcher3.group(4) +
									matcher3.group(5) + matcher3.group(6) +
									matcher3.group(7) + matcher3.group(8) +
									matcher3.group(9) + matcher3.group(10) +
									matcher3.group(11) + matcher3.group(12) +
									matcher3.group(13) + matcher3.group(14) +
									matcher3.group(15);
							System.out.println("0 babcao");
						}
						else if (matcher2.find() && etapa == 1) {
							System.out.println("aqqqqq");
							if (matcher2.group(1) != null) parteDecreto = "O ";
							parteDecreto = parteDecreto + matcher2.group(2) + matcher2.group(3) +
									matcher2.group(4) + matcher2.group(5) + matcher2.group(6) +
									matcher2.group(7);

							if (matcher2.group(9) != null) parteDecreto = parteDecreto + matcher2.group(9);
							if (matcher2.group(10) != null) parteDecreto = parteDecreto + matcher2.group(10);
							if (matcher2.group(12) != null) parteDecreto = parteDecreto + matcher2.group(12);
							if (matcher2.group(13) != null) parteDecreto = parteDecreto + matcher2.group(13);
							System.out.println("um babcao");
						}else if (matcher1.find() && etapa == 1){
							System.out.println("entrou nessa pora");
							if (matcher1.group(1) != null) parteDecreto = "O ";
							parteDecreto = parteDecreto +	matcher1.group(2);
							if (matcher1.group(5) != null) parteDecreto = parteDecreto + matcher1.group(5);
							if (matcher1.group(9) != null) parteDecreto = parteDecreto + matcher1.group(9);
							if (matcher1.group(15) != null) parteDecreto = parteDecreto + matcher1.group(15);
							if (matcher1.group(17) != null) parteDecreto = parteDecreto + matcher1.group(17);
							if (matcher1.group(20) != null) parteDecreto = parteDecreto + matcher1.group(20);
							if (matcher1.group(21) != null) parteDecreto = parteDecreto + matcher1.group(21);
							if (matcher1.group(23) != null) parteDecreto = parteDecreto + matcher1.group(23);
							if (matcher1.group(25)!= null){
								if (matcher1.group(26) != null) parteDecreto = parteDecreto + matcher1.group(26);
								if (matcher1.group(27) != null) parteDecreto = parteDecreto + matcher1.group(27);
								if (matcher1.group(28) != null) parteDecreto = parteDecreto + matcher1.group(28);
								if (matcher1.group(29) != null) parteDecreto = parteDecreto + matcher1.group(29);
								if (matcher1.group(30) != null) parteDecreto = parteDecreto + matcher1.group(30);
								if (matcher1.group(31) != null) parteDecreto = parteDecreto + matcher1.group(31);
								if (matcher1.group(32) != null) parteDecreto = parteDecreto + matcher1.group(32);
							}
						}
						 */	
					}


				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				//decreto = decreto.replaceAll("(\u0009)+", "\n\t");

				try {
					fw = new FileWriter(new File(arquivoPasta + "\\\\" + nomeArquivo));
					bw = new BufferedWriter(fw);
					bw.write(parteDecreto);
					bw.flush();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//System.out.println(i + " " + j + " foi xmlizado");
				parteDecreto = "";
			}
		}

	}

}
