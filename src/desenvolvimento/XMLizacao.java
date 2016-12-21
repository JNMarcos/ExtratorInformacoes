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
		String pastaOrigem = "semFormataÁ„o";

		//Caminhos dos decretos
		//Substitua para a localizaÁ„o em seu computador
		String caminhoDecretos = "C:\\Users\\JN\\Documents\\DecretosAlepe\\";
		int[] anosDecretos = {2014, 2015, 2016};

		//"Cria-se" os Files que apontam para as pastas dos decretos
		File[] pastas = new File[3];
		pastas[0] = new File(caminhoDecretos + anosDecretos[0] + "\\" + pastaOrigem);
		pastas[1] = new File(caminhoDecretos + anosDecretos[1] + "\\" + pastaOrigem);
		pastas[2] = new File(caminhoDecretos + anosDecretos[2] + "\\" + pastaOrigem);

		//Usado para conter a lista de endereÁos dos arquivos dentro de cada pasta
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
		FileWriter fwarq;
		BufferedWriter bwarq = null;
		
		String nomeArquivo;
		File arquivoPasta;
		Map<String, Integer> contadorTiposDecreto = new Hashtable<>();

		//Regex
		//\u00B2\u00B3\u00B7
		String regexIdentificacao = "(DECRETO N∫)\\s*(\\d{2}.\\d{3}, (DE|DIA) \\d{1,2}(∞|∫)? DE [A-Z«]+ DE \\d{4}.)";
		String regexTipoDecreto = "(Abre|Aloca|Altera|Amplia|Aprova|Autoriza|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Desativa|Disciplina|Dispıe|Eleva|Estabelece|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([A-Za-z0-9Á„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\(\\)!?&$%:;,∫∞™ \n\t/\u002E\u00A7\u002D]+)(O )?(GOVERNADOR|GOVERNO)";
		String regexTipoDecreto2 = "(Abre|Aloca|Altera|Amplia|Aprova|Autoriza|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Desativa|Disciplina|Dispıe|Eleva|Estabelece|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([A-Za-z0-9Á„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\(\\)!?$%:;,∫∞™ \n\t/\u002E\u00A7\u002D]+)(O )?(PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERCÕCIO DO CARGO DE GOVERNADOR DO ESTADO)";
		String regexAtribuicoes = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO)([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'!?$%&0-9:;∫∞™\\(\\), \n\t/\u002E\u00A7\u002D]+)(CONSIDERANDO|DECRETA)";
		String regexAtribuicoes2 = "(O )?(PRESIDENTE DA ASSEMBLEIA LEGISLATIVA)( DO ESTADO)?( DE PERNAMBUCO)([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"!?$%0-9:;∫∞™\\(\\), \n\t/\u002E\u00A7\u002D]+)(CONSIDERANDO|DECRETA)";
		String regexConsideracoes = "(CONSIDERANDO)([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\(\\)!?$%0-9:;∫∞™, \n\t/\u002E\u00A7\u002D]+)(DECRETA)";
		String regexCorpo = "(DECRETA)([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\(\\)!?$%0-9:;∫∞™, \n\t/\u002E\u00A7\u002D]+)(Pal·cio do Campo das Princesas)";
		String regexFinalmentes = "(O )?(Pal·cio do Campo das Princesas)([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"!?$%0-9:;∫∞™, \n\t/\\(\\)\u002E\u00A7\u002D\u00B2\u00B3\u00B7]+)(IndependÍncia do Brasil\u002E)";
		String regexAssinaturas = "([A-Z¬√¡¿… Õ”‘’⁄« \n\t]+)(Governador do Estado)( em exercÌcio)?([A-Z¬√¡¿… Õ”‘’⁄« \n\t]+)(ANEXO)?";

		//Padrıes e Matchers
		Pattern padraoIdentificacao = Pattern.compile(regexIdentificacao);
		Pattern padraoTipoDecreto = Pattern.compile(regexTipoDecreto);
		Pattern padraoTipoDecreto2 = Pattern.compile(regexTipoDecreto2);
		Pattern padraoAtribuicoes = Pattern.compile(regexAtribuicoes);
		Pattern padraoAtribuicoes2 = Pattern.compile(regexAtribuicoes2);
		Pattern padraoConsideracoes = Pattern.compile(regexConsideracoes);
		Pattern padraoCorpo = Pattern.compile(regexCorpo);
		Pattern padraoFinalmentes = Pattern.compile(regexFinalmentes);
		Pattern padraoAssinaturas = Pattern.compile(regexAssinaturas);

		Matcher matcher;
		Matcher matcherAuxiliar;

		File arquivoRegexNaoCaptura = new File("regexNaoCaptura");
		try {
			arquivoRegexNaoCaptura.createNewFile();
			fwarq = new FileWriter(arquivoRegexNaoCaptura);
			bwarq = new BufferedWriter(fwarq);
		} catch (IOException e1) {
			System.out.println("Arquivo n„o criado.");
		}

		String decretoIntermediario = "";
		String decretoSaida = "";
		String decreto;
		String linha;


		//AQUI COME«A A REMO«√O DA FORMATA«√O
		for (int i = 0; i < anosDecretos.length; i++){
			arquivoPasta = new File(caminhoDecretos + anosDecretos[i] + "\\" + pastaDestino);
			System.out.println(arquivoPasta);
			if (!arquivoPasta.exists()){ //Se pasta n„o existe, cria
				arquivoPasta.mkdir();
			}

			//tem de add ativa ainda
			contadorTiposDecreto.put("Abre", 0);
			contadorTiposDecreto.put("Aloca", 0);
			contadorTiposDecreto.put("Altera", 0);
			contadorTiposDecreto.put("Amplia", 0);
			contadorTiposDecreto.put("Aprova", 0);
			contadorTiposDecreto.put("Autoriza", 0);
			contadorTiposDecreto.put("Atualiza", 0);
			contadorTiposDecreto.put("Concede", 0);
			contadorTiposDecreto.put("Convoca", 0);
			contadorTiposDecreto.put("Cria", 0);
			contadorTiposDecreto.put("Declara", 0);
			contadorTiposDecreto.put("Decreta", 0);
			contadorTiposDecreto.put("Define", 0);
			contadorTiposDecreto.put("Desativa", 0);
			contadorTiposDecreto.put("Disciplina", 0);
			contadorTiposDecreto.put("Dispıe", 0);
			contadorTiposDecreto.put("Eleva", 0);
			contadorTiposDecreto.put("Estabelece", 0);
			contadorTiposDecreto.put("Homologa", 0);
			contadorTiposDecreto.put("Incorpora", 0);
			contadorTiposDecreto.put("Institui", 0);
			contadorTiposDecreto.put("Interpreta", 0);
			contadorTiposDecreto.put("Introduz", 0);
			contadorTiposDecreto.put("Modifica", 0);
			contadorTiposDecreto.put("Promove", 0);
			contadorTiposDecreto.put("Prorroga", 0);
			contadorTiposDecreto.put("Qualifica", 0);
			contadorTiposDecreto.put("Redenomina", 0);
			contadorTiposDecreto.put("Regulamenta", 0);
			contadorTiposDecreto.put("Relaciona", 0);
			contadorTiposDecreto.put("Renova", 0);
			contadorTiposDecreto.put("Revoga", 0);
			contadorTiposDecreto.put("Transfere", 0);
			contadorTiposDecreto.put("Transforma", 0);

			for (int j = 0; j < arquivos[i].length; j++){
				nomeArquivo = (arquivos[i][j]).getName(); //obtÈm o nome do arquivo
				System.out.println(nomeArquivo);
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

					decretoIntermediario = decreto;

					matcher = padraoIdentificacao.matcher(decretoIntermediario);
					if (matcher.find()){
						decretoSaida += "<IDENTIFICACAO>" + 
								matcher.group(1) + matcher.group(2) + "</IDENTIFICACAO>";
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "IDENTIFICACAO");
						bwarq.newLine();
					}

					matcher = padraoTipoDecreto.matcher(decretoIntermediario);
					matcherAuxiliar = padraoTipoDecreto2.matcher(decretoIntermediario);
					if (matcherAuxiliar.find()){
						decretoSaida += "<DESCRICAO>" +
								matcherAuxiliar.group(1) + " " + matcherAuxiliar.group(2) + "<";
						if (contadorTiposDecreto.containsKey(matcherAuxiliar.group(1))){
							contadorTiposDecreto.put(matcherAuxiliar.group(1), contadorTiposDecreto.get(matcherAuxiliar.group(1)) + 1);
						} else{
							contadorTiposDecreto.put(matcherAuxiliar.group(1), 1);
						}
					} else if (matcher.find()){
						decretoSaida += "<DESCRICAO>" +
								matcher.group(1) + " " + matcher.group(2) + "<";

						if (contadorTiposDecreto.containsKey(matcher.group(1))){
							contadorTiposDecreto.put(matcher.group(1), contadorTiposDecreto.get(matcher.group(1)) + 1);
						} else{
							contadorTiposDecreto.put(matcher.group(1), 1);
						}
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "DESCRICAO");
						bwarq.newLine();
					}

					decretoSaida = decretoSaida.replaceFirst("O\\s*<", "");
					decretoSaida = decretoSaida.trim();
					decretoSaida += "</DESCRICAO>";

					matcher = padraoAtribuicoes.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAtribuicoes2.matcher(decretoIntermediario);

					decretoSaida += "<ATRIBUICAO>";
					if (matcherAuxiliar.find()){
						if (matcherAuxiliar.group(1) != null)
							decretoSaida += matcherAuxiliar.group(1);
						decretoSaida += matcherAuxiliar.group(2);
						if (matcherAuxiliar.group(3) != null)
							decretoSaida += matcherAuxiliar.group(3);
						decretoSaida += matcherAuxiliar.group(4) + matcherAuxiliar.group(5);
					} else if (matcher.find()){
						if (matcher.group(1) != null)
							decretoSaida += matcher.group(1);
						decretoSaida += matcher.group(2) + matcher.group(3);
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "ATRIBUICAO");
						bwarq.newLine();
					}

					decretoSaida = decretoSaida.trim();
					decretoSaida += "</ATRIBUICAO>";

					matcher = padraoConsideracoes.matcher(decretoIntermediario);
					matcherAuxiliar = null;
					if (matcher.find()){
						decretoSaida += "<CONSIDERACOES>" + matcher.group(1) 
								+ matcher.group(2) + "</CONSIDERACOES>";
					} else if (decretoIntermediario.contains("CONSIDERANDO")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "CONSIDERACOES");
						bwarq.newLine();
					}

					matcher = padraoCorpo.matcher(decretoIntermediario);
					if (matcher.find()){
						decretoSaida += "<CORPO>" + matcher.group(1)
								+ matcher.group(2) + "</CORPO>";
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "CORPO");
						bwarq.newLine();
					}

					matcher = padraoFinalmentes.matcher(decretoIntermediario);
					if (matcher.find()){
						decretoSaida += "<FINALMENTES>";
						if (matcher.group(1) != null) decretoSaida +=  matcher.group(1).trim();
						decretoSaida += matcher.group(2).trim() + " " + matcher.group(3).trim() + matcher.group(4).trim() +  "</FINALMENTES>";
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "FINALMENTES");
						bwarq.newLine();
					}

					matcher = padraoAssinaturas.matcher(decretoIntermediario);
					if (matcher.find()){
						decretoSaida += "<ASSINATURAS>" + matcher.group(1).trim()
								+ matcher.group(2).trim();
						if (matcher.group(3) != null) decretoSaida += matcher.group(3);
						decretoSaida +=  matcher.group(4) + "</ASSINATURAS>";
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "ASSINATURAS");
						bwarq.newLine();
					}

					decretoSaida = decretoSaida.replaceAll("\t", "");
					decretoSaida = decretoSaida.replaceAll("\u0020,", ",");

					File a = new File("decretos" + anosDecretos[i]);
					a.createNewFile();
					fw = new FileWriter(a);
					bw = new BufferedWriter(fw);
					List<String> contador = new ArrayList<>(contadorTiposDecreto.keySet());
					for (int v = 0; v < contador.size(); v++){	
						bw.write(contador.get(v) + "    " + contadorTiposDecreto.get(contador.get(v)));
						bw.newLine();
					}
					bw.flush();
					bw.close();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					fw = new FileWriter(new File(arquivoPasta + "\\\\" + nomeArquivo));
					bw = new BufferedWriter(fw);
					bw.write(decretoSaida);
					bw.flush();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				decretoSaida = "";
			}
		}

		try {
			bwarq.flush();
			bwarq.close();
		} catch (IOException e) {
			System.out.println("Erro no fechamento do streaming.");
		}


	}

}
