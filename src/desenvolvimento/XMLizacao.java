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
		String pastaOrigem = "semFormatação";

		//Caminhos dos decretos
		//Substitua para a localização em seu computador
		String caminhoDecretos = "C:\\Users\\JN\\Documents\\DecretosAlepe\\";
		int[] anosDecretos = {2014, 2015, 2016};

		//"Cria-se" os Files que apontam para as pastas dos decretos
		File[] pastas = new File[3];
		pastas[0] = new File(caminhoDecretos + anosDecretos[0] + "\\" + pastaOrigem);
		pastas[1] = new File(caminhoDecretos + anosDecretos[1] + "\\" + pastaOrigem);
		pastas[2] = new File(caminhoDecretos + anosDecretos[2] + "\\" + pastaOrigem);

		//Usado para conter a lista de endereços dos arquivos dentro de cada pasta
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
		String regexIdentificacao = "(DECRETO Nº)\\s+(\\d{2}.\\d{3}(,|.)? (DE|DIA) \\d{1,2}(°|º)? DE [A-ZÇ]+ DE \\d{4}.)";
		String regexTipoDecreto = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Dispõe|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco)";
		String regexTipoDecreto2 = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Dispõe|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERCÍCIO DO CARGO DE GOVERNADOR DO ESTADO)";
		String regexAtribuicoes = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(CONSIDERANDO|DECRETA|DECRETO)";
		String regexAtribuicoes2 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAtribuicoes3 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(Art)";
		String regexConsideracoes = "(CONSIDERANDO)([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,§º°ª./<>-_\\\\E \n\t\u00A7\u002D]+)(DECRETA)";
		String regexCorpo = "(DECRETA|DECRETO[^ N°]{3})([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(Palácio do Campo das Princesas)";
		String regexCorpo4 = "(DECRETA|DECRETO[^ N°]{3})([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(Palácio do Campo das Princesas[A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ ,º0-9\\.]+(ANEXO))";
		String regexCorpo2 = "(DECRETA|DECRETO[^ N°]{3})([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$%:@#;,/§º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexCorpo3 = "(Art)([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$§%:@#;,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(Palácio do Campo das Princesas)";
		String regexFinalmentes = "(O )?(Palácio do Campo das Princesas)([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$§%:@#;,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(Independência do Brasil(\\.)?)";
		String regexAssinaturas = "([A-ZÂÃÁÀÉÊÍÓÔÕÚÇ \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exercício)?([A-ZÂÃÁÀÉÊÍÓÔÕÚÇ \']+)";
		String regexAssinaturas2 = "([A-ZÂÃÁÀÉÊÍÓÔÕÚÇ \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exercício)?([A-ZÂÃÁÀÉÊÍÓÔÕÚÇ \']+)(ANEXO)";
		String regexAnexos = "(NO )?(ANEXO)([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$§%:@#;/,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAnexos2 = "(NO )?(ANEXO)([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$§%:@#;/,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)(ERRATA [^PUBLI]+)";
		String regexErrata = "(ERRATA)([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$§%:@#;/,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexInfo = "(\\Q(ERRATA PUBLICADA NO DIÁRIO OFICIAL DE \\E\\d{1,2} DE [A-ZÇ]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SAÍDO COM INCORREÇÃO NO ORIGINAL)\\E)";

		String regexConsiderando = "<CONSIDERACOES>([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$§%:@#;/,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)</CONSIDERACOES>";
		String regexAssinatura = "<ASSINATURAS>([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q()!?&$§%:@#;/,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)</ASSINATURAS>";
		String regexLeis = "( Decreto| \\QDecreto-Lei\\E| Decreto Lei| Decreto Lei Federal| Decreto-Lei Federal| Lei Federal| Lei Complementar| Lei| Resolução| Emenda Constitucional| Ad Referendum| Ofício| Parecer| Parecer Conjunto| Portaria Conjunta| Portaria)( (nº|n°) (\\d{1,3}\\.\\d{3}|\\d{3}|\\d{2}|\\d{2,4}/([A-Z /-]{0,16}) \\d{2,4} [A-Z/ -]{0,8}), (de (\\d{1,2}) de ([a-zçA-ZÇ]{4,9}) de (\\d{4}))?)( da Constituição Estadual)?";
		String regexAnexo = "<ANEXOS>([0-9A-Za-zçãàáâéêíóôõúÂÃÁÀÉÊÍÓÔÕÚÇ\"\'\\Q(!?)&$§%:@#;/,º°ª.<>-_\\\\E \n\t\u00A7\u002D]+)</ANEXOS>";
		
		//Padrões e Matchers
		Pattern padraoIdentificacao = Pattern.compile(regexIdentificacao);
		Pattern padraoTipoDecreto = Pattern.compile(regexTipoDecreto);
		Pattern padraoTipoDecreto2 = Pattern.compile(regexTipoDecreto2);
		Pattern padraoAtribuicoes = Pattern.compile(regexAtribuicoes);
		Pattern padraoAtribuicoes2 = Pattern.compile(regexAtribuicoes2);
		Pattern padraoAtribuicoes3 = Pattern.compile(regexAtribuicoes3);
		Pattern padraoConsideracoes = Pattern.compile(regexConsideracoes);
		Pattern padraoCorpo = Pattern.compile(regexCorpo);
		Pattern padraoCorpo2 = Pattern.compile(regexCorpo2);
		Pattern padraoCorpo3 = Pattern.compile(regexCorpo3);
		Pattern padraoCorpo4 = Pattern.compile(regexCorpo4);
		Pattern padraoFinalmentes = Pattern.compile(regexFinalmentes);
		Pattern padraoAssinaturas = Pattern.compile(regexAssinaturas);
		Pattern padraoAssinaturas2 = Pattern.compile(regexAssinaturas2);
		Pattern padraoAnexos = Pattern.compile(regexAnexos);
		Pattern padraoAnexos2 = Pattern.compile(regexAnexos2);
		Pattern padraoErrata = Pattern.compile(regexErrata);
		Pattern padraoInfo = Pattern.compile(regexInfo);

		Pattern padraoConsiderando = Pattern.compile(regexConsiderando);
		Pattern padraoAssinatura = Pattern.compile(regexAssinatura);
		Pattern padraoLeis = Pattern.compile(regexLeis);
		Pattern padraoAnexo = Pattern.compile(regexAnexo);

		Matcher matcher;
		Matcher matcherAuxiliar;

		Hashtable<String, String> tiposDocumentos = new Hashtable<>();
		tiposDocumentos.put("Lei", "LE");
		tiposDocumentos.put("Lei Complementar", "LC");
		tiposDocumentos.put("Lei Federal", "LF");
		tiposDocumentos.put("Decreto", "DE");
		tiposDocumentos.put("Decreto-Lei", "DL");
		tiposDocumentos.put("Decreto Lei", "DL");
		tiposDocumentos.put("Decreto-Lei Federal", "DLF");
		tiposDocumentos.put("Decreto Lei Federal", "DLF");
		tiposDocumentos.put("Resolução", "RE");
		tiposDocumentos.put("Emenda Constitucional", "EC");
		tiposDocumentos.put("Ad Referendum", "AR");
		tiposDocumentos.put("Ofício", "OF");
		tiposDocumentos.put("Parecer", "PAR");
		tiposDocumentos.put("Parecer  Conjunto", "PC");
		tiposDocumentos.put("Portaria", "Pt");
		tiposDocumentos.put("Portaria  Conjunta", "PtC");

		Hashtable<String, String> numeracaoMeses = new Hashtable<>();
		numeracaoMeses.put("janeiro", "01");
		numeracaoMeses.put("fevereiro", "02");
		numeracaoMeses.put("março", "03");
		numeracaoMeses.put("abril", "04");
		numeracaoMeses.put("maio", "05");
		numeracaoMeses.put("junho", "06");
		numeracaoMeses.put("julho", "07");
		numeracaoMeses.put("agosto", "08");
		numeracaoMeses.put("setembro", "09");
		numeracaoMeses.put("outubro", "10");
		numeracaoMeses.put("novembro", "11");
		numeracaoMeses.put("dezembro", "12");

		File arquivoRegexNaoCaptura = new File("regexNaoCaptura");
		try {
			arquivoRegexNaoCaptura.createNewFile();
			fwarq = new FileWriter(arquivoRegexNaoCaptura);
			bwarq = new BufferedWriter(fwarq);
		} catch (IOException e1) {
			System.out.println("Arquivo não criado.");
		}

		String decretoIntermediario = "";
		String decretoSaida = "";
		String decreto;
		String linha;


		//AQUI COMEÇA A REMOÇÃO DA FORMATAÇÃO
		for (int i = 0; i < anosDecretos.length; i++){
			arquivoPasta = new File(caminhoDecretos + anosDecretos[i] + "\\" + pastaDestino);
			System.out.println(arquivoPasta);
			if (!arquivoPasta.exists()){ //Se pasta não existe, cria
				arquivoPasta.mkdir();
			}

			//tem de add ativa ainda
			contadorTiposDecreto.put("Abre", 0);
			contadorTiposDecreto.put("Acrescenta", 0);
			contadorTiposDecreto.put("Aloca", 0);
			contadorTiposDecreto.put("Altera", 0);
			contadorTiposDecreto.put("Amplia", 0);
			contadorTiposDecreto.put("Aprova", 0);
			contadorTiposDecreto.put("Autoriza", 0);
			contadorTiposDecreto.put("Atualiza", 0);
			contadorTiposDecreto.put("Ativa", 0);
			contadorTiposDecreto.put("Concede", 0);
			contadorTiposDecreto.put("Convoca", 0);
			contadorTiposDecreto.put("Cria", 0);
			contadorTiposDecreto.put("Declara", 0);
			contadorTiposDecreto.put("Decreta", 0);
			contadorTiposDecreto.put("Define", 0);
			contadorTiposDecreto.put("Delega", 0);
			contadorTiposDecreto.put("Desativa", 0);
			contadorTiposDecreto.put("Disciplina", 0);
			contadorTiposDecreto.put("Dispõe", 0);
			contadorTiposDecreto.put("Eleva", 0);
			contadorTiposDecreto.put("Estabelece", 0);
			contadorTiposDecreto.put("Estende", 0);
			contadorTiposDecreto.put("Homologa", 0);
			contadorTiposDecreto.put("Incorpora", 0);
			contadorTiposDecreto.put("Institui", 0);
			contadorTiposDecreto.put("Interpreta", 0);
			contadorTiposDecreto.put("Introduz", 0);
			contadorTiposDecreto.put("Modifica", 0);
			contadorTiposDecreto.put("Promove", 0);
			contadorTiposDecreto.put("Prorroga", 0);
			contadorTiposDecreto.put("Qualifica", 0);
			contadorTiposDecreto.put("Reabre", 0);
			contadorTiposDecreto.put("Redenomina", 0);
			contadorTiposDecreto.put("Regulamenta", 0);
			contadorTiposDecreto.put("Relaciona", 0);
			contadorTiposDecreto.put("Renova", 0);
			contadorTiposDecreto.put("Revoga", 0);
			contadorTiposDecreto.put("Transfere", 0);
			contadorTiposDecreto.put("Transforma", 0);

			for (int j = 0; j < arquivos[i].length; j++){
				nomeArquivo = (arquivos[i][j]).getName(); //obtém o nome do arquivo
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
					decretoSaida = "";
					matcher = padraoInfo.matcher(decretoIntermediario);
					
					if (matcher.find()){
						decretoSaida += "<INFO>" + matcher.group(0) + "</INFO>";
					}

					matcher = padraoIdentificacao.matcher(decretoIntermediario);
					matcherAuxiliar = null;

					if (matcher.find()){
						decretoSaida += "<IDENTIFICACAO>" + 
								matcher.group(1).trim() + " " + matcher.group(2) + "</IDENTIFICACAO>";
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "IDENTIFICACAO");
						bwarq.newLine();
					}

					matcher = padraoTipoDecreto.matcher(decretoIntermediario);
					matcherAuxiliar = padraoTipoDecreto2.matcher(decretoIntermediario);

					if (matcherAuxiliar.find()){
						decretoSaida += "<DESCRICAO>" +
								matcherAuxiliar.group(1) + " " + matcherAuxiliar.group(2) + "<";
						contadorTiposDecreto.containsKey(matcherAuxiliar.group(1));
						contadorTiposDecreto.put(matcherAuxiliar.group(1), contadorTiposDecreto.get(matcherAuxiliar.group(1)) + 1);

					} else if (matcher.find()){
						decretoSaida += "<DESCRICAO>" +
								matcher.group(1) + " " + matcher.group(2) + "<";

						contadorTiposDecreto.containsKey(matcher.group(1));
						contadorTiposDecreto.put(matcher.group(1), contadorTiposDecreto.get(matcher.group(1)) + 1);

					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "DESCRICAO");
						bwarq.newLine();
					}

					decretoSaida = decretoSaida.replaceFirst("O\\s*<", " ");
					String[] partes = decretoSaida.split("GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO");
					decretoSaida = partes[0].trim();
					decretoSaida += "</DESCRICAO>";

					matcher = padraoAtribuicoes.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAtribuicoes2.matcher(decretoIntermediario);

					decretoSaida += "<ATRIBUICAO>";
					if (matcher.find()){
						if (matcher.group(1) != null)
							decretoSaida += matcher.group(1).trim() + " ";
						decretoSaida += matcher.group(2).trim() + " " + matcher.group(5).trim();
					} else if (matcherAuxiliar.find()){
						if (matcherAuxiliar.group(1) != null)
							decretoSaida += matcherAuxiliar.group(1).trim() + " ";
						decretoSaida += matcherAuxiliar.group(2).trim() + " " + matcherAuxiliar.group(5).trim();
					} else{
						matcherAuxiliar = padraoAtribuicoes3.matcher(decretoIntermediario);
						if (matcherAuxiliar.find()){
							if (matcherAuxiliar.group(1) != null)
								decretoSaida += matcherAuxiliar.group(1).trim() + " ";
							decretoSaida += matcherAuxiliar.group(2).trim() + " " + matcherAuxiliar.group(5).trim();
						} else { 
							bwarq.write(i + "     " + nomeArquivo + "     " + "ATRIBUICAO");
							bwarq.newLine();
						}
					}

					//se pegar os considerandos para no primeiro
					partes = decretoSaida.split("CONSIDERANDO");
					decretoSaida = partes[0].trim();
					decretoSaida += "</ATRIBUICAO>";

					matcher = padraoConsideracoes.matcher(decretoIntermediario);
					matcherAuxiliar = null;

					if (matcher.find()){
						decretoSaida += "<CONSIDERACOES>" + matcher.group(1).trim() + " " 
								+ matcher.group(2).trim() + "</CONSIDERACOES>";
					} else if (decretoIntermediario.contains("CONSIDERANDO")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "CONSIDERACOES");
						bwarq.newLine();
					}

					matcher = padraoCorpo4.matcher(decretoIntermediario);
					matcherAuxiliar = padraoCorpo.matcher(decretoIntermediario);

					if (matcher.find()){
						decretoSaida += "<CORPO>" + matcher.group(1).trim() + " "
								+ matcher.group(2).trim() + "</CORPO>";

					} else if (matcherAuxiliar.find()){
						decretoSaida += "<CORPO>" + matcherAuxiliar.group(1).trim() + " "
								+ matcherAuxiliar.group(2).trim() + "</CORPO>";
					} else{
						matcher = padraoCorpo2.matcher(decretoIntermediario);
						matcherAuxiliar = padraoCorpo3.matcher(decretoIntermediario);
						if (matcher.find()) {
							decretoSaida += "<CORPO>" + matcher.group(1).trim() + " "
									+ matcher.group(2).trim() + "</CORPO>";
						} else if (matcherAuxiliar.find()){
							decretoSaida += "<CORPO>" + matcherAuxiliar.group(1).trim() + " "
									+ matcherAuxiliar.group(2).trim() + "</CORPO>";			
						}else{
							bwarq.write(i + "     " + nomeArquivo + "     " + "CORPO");
							bwarq.newLine();
						}
					}

					matcher = padraoFinalmentes.matcher(decretoIntermediario);
					matcherAuxiliar = null;

					if (matcher.find()){
						decretoSaida += "<FINALMENTES>\n";
						if (matcher.group(1) != null) decretoSaida +=  matcher.group(1).trim();
						decretoSaida += matcher.group(2).trim() + " " + matcher.group(3).trim() + " " +
								matcher.group(4).trim() +  "\n</FINALMENTES>";
					} else if (decretoIntermediario.contains("Palácio do Campo")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "FINALMENTES");
						bwarq.newLine();
					}

					matcher = padraoAssinaturas.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAssinaturas2.matcher(decretoIntermediario);

					if (matcherAuxiliar.find()){
						decretoSaida += "<ASSINATURAS>" + matcherAuxiliar.group(1).trim() + " " 
								+ matcherAuxiliar.group(2);
						if (matcherAuxiliar.group(3) != null) decretoSaida += " " + matcherAuxiliar.group(3);
						decretoSaida += "  " + matcherAuxiliar.group(4).replaceFirst("ERRATA[ A-ZÂÃÁÀÉÊÍÓÔÕÚÇ]+","").trim() + "</ASSINATURAS>";
					} else if (matcher.find()){
						decretoSaida += "<ASSINATURAS>" + matcher.group(1).trim() + " "
								+ matcher.group(2);
						if (matcher.group(3) != null) decretoSaida += " " + matcher.group(3);
						decretoSaida +=  "  " + matcher.group(4).replaceFirst("ERRATA[ A-ZÂÃÁÀÉÊÍÓÔÕÚÇ]+","").trim() + "</ASSINATURAS>";
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "ASSINATURAS");
						bwarq.newLine();
					}

					matcher = padraoAnexos.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAnexos2.matcher(decretoIntermediario);

					if (matcherAuxiliar.find()){
						if(!matcherAuxiliar.group(0).startsWith("NO ")){
							decretoSaida += "<ANEXOS>" + matcherAuxiliar.group(2).trim() + " "
									+ matcherAuxiliar.group(3).trim() + "</ANEXOS>";
						}
					} else if (matcher.find()){
						if(!matcher.group(0).startsWith("NO ")){
							decretoSaida += "<ANEXOS>" + matcher.group(2).trim() + " "
									+ matcher.group(3).trim() + "</ANEXOS>";
						}
					} else if (decretoIntermediario.contains("ANEXO")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "ANEXOS");
						bwarq.newLine();
					}


					matcher = padraoErrata.matcher(decretoIntermediario);
					matcherAuxiliar = null;

					if (matcher.find()){
						decretoSaida += "<ERRATA>" + matcher.group(1).trim() + " "
								+ matcher.group(2).replaceFirst("(\\Q(ERRATA PUBLICADA NO DIÁRIO OFICIAL DE \\E\\d{1,2} DE [A-ZÇ]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SAÍDO COM INCORREÇÃO NO ORIGINAL)\\E)" , "").trim()
								+ "</ERRATA>";
					} 

					matcher = padraoConsiderando.matcher(decretoSaida);
					String entreTags;
					String textoModificado;
					String[] segmentosConsAss;
					if (matcher.find()){
						entreTags = matcher.group(1);
						segmentosConsAss = entreTags.split("( )+CONSIDERANDO");
						textoModificado = "";
						for (int k = 0; k < segmentosConsAss.length; k++){
							if (k != 0)
								textoModificado += "<CONSIDERANDO>" + ("CONSIDERANDO" + segmentosConsAss[k]).trim() + "</CONSIDERANDO>";
							else
								textoModificado += "<CONSIDERANDO>" + segmentosConsAss[k].trim() + "</CONSIDERANDO>";
						}
						decretoSaida = decretoSaida.replaceFirst(entreTags, textoModificado);
					}

					matcher = padraoAssinatura.matcher(decretoSaida);
					if (matcher.find()){
						entreTags = matcher.group(1);
						segmentosConsAss = entreTags.split("( ){2,}");
						textoModificado = "";
						textoModificado += "<ASSINATURA_GOV>" + segmentosConsAss[0].trim() + "</ASSINATURA_GOV>";
						for (int k = 1; k < segmentosConsAss.length; k++){
							//System.out.println(segmentosConsAss[k]);
							textoModificado += "<ASSINATURA>" + segmentosConsAss[k].trim() + "</ASSINATURA>";
						}
						decretoSaida = decretoSaida.replace(entreTags, textoModificado);
					}

					matcher = padraoLeis.matcher(decretoSaida);

					while (matcher.find()){
						entreTags = matcher.group(0);
						//System.out.println(entreTags);
						textoModificado = " <" +  tiposDocumentos.get(matcher.group(1).trim())+ " numeracao=" + matcher.group(4);
						if (matcher.group(6) != null){
								textoModificado += " data=" + matcher.group(7)+ "-" + numeracaoMeses.get(matcher.group(8).toLowerCase()) + "-" + matcher.group(9);
						}
						textoModificado+= ">" + 	matcher.group(0).trim() + "</" + tiposDocumentos.get(matcher.group(1).trim()) + ">";
						decretoSaida = decretoSaida.replace(entreTags, textoModificado);
					}

					//TÁ DANDO ERRO AQUI
					//QUALQUER COISA TIRAR
					matcher = padraoAnexo.matcher(decretoSaida);
					textoModificado = "";
					if (matcher.find()){
						entreTags = matcher.group(1);
						if (entreTags.startsWith("ANEXO ÚNICO")){
							System.out.println(entreTags);
							if (entreTags.contains("MEMORIAL DESCRITIVO"))
								textoModificado = "<MEMO>" + entreTags + "</MEMO>";
							else if (entreTags.contains("table"))
								textoModificado = "<TABELA>" + entreTags + "</TABELA>";
							else
								textoModificado = "<OUTRO>" + entreTags + "</OUTRO>";
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
						} else {
							segmentosConsAss = entreTags.split("ANEXO [IVX]{1,3}");
							
							for (int k = 1; k < segmentosConsAss.length; k++){
								if (entreTags.contains("MEMORIAL DESCRITIVO"))
									textoModificado += "<MEMO>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</MEMO>";
								else if (entreTags.contains("table"))
									textoModificado += "<TABELAS>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</TABELAS>";
								else
									textoModificado += "<OUTRO>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</OUTRO>";
							}
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
						}
					}
					
					decretoSaida = decretoSaida.replaceAll("&middot;", "\u00B7");
					decretoSaida = decretoSaida.replaceAll("&sup2;","\u00B2");
					decretoSaida = decretoSaida.replaceAll("&sup3;","\u00B3");


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
					fw.write(decretoSaida);
					//bw = new BufferedWriter(fw);
					//bw.write(decretoSaida);
					fw.flush();//antes os dois era bw
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
