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
		String pastaOrigem = "semFormata��o";

		//Caminhos dos decretos
		//Substitua para a localiza��o em seu computador
		String caminhoDecretos = "C:\\Users\\JN\\Documents\\DecretosAlepe\\";
		int[] anosDecretos = {2014, 2015, 2016};

		//"Cria-se" os Files que apontam para as pastas dos decretos
		File[] pastas = new File[3];
		pastas[0] = new File(caminhoDecretos + anosDecretos[0] + "\\" + pastaOrigem);
		pastas[1] = new File(caminhoDecretos + anosDecretos[1] + "\\" + pastaOrigem);
		pastas[2] = new File(caminhoDecretos + anosDecretos[2] + "\\" + pastaOrigem);

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
		FileWriter fwb;
		BufferedWriter bwb = null;
		FileWriter fwc;
		BufferedWriter bwc = null;
		FileWriter fwarq;
		BufferedWriter bwarq = null;

		String nomeArquivo;
		File arquivoPasta;
		Map<String, Integer> contadorTiposDecreto = new Hashtable<>();

		//Regex
		//\u00B2\u00B3\u00B7
		String regexIdentificacao = "(DECRETO N�)\\s+(\\d{2}.\\d{3}(,|.)? (DE|DIA) \\d{1,2}(�|�)? DE [A-Z�]+ DE \\d{4}.)";
		String regexTipoDecreto = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Disp�e|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco)";
		String regexTipoDecreto2 = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Disp�e|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERC�CIO DO CARGO DE GOVERNADOR DO ESTADO)";
		String regexAtribuicoes = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(CONSIDERANDO|DECRETA|DECRETO)";
		String regexAtribuicoes2 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAtribuicoes3 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(Art)";
		String regexConsideracoes = "(CONSIDERANDO)([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,����./<>-_\\\\E \n\t\u00A7\u002D]+)(DECRETA)";
		String regexCorpo = "(DECRETA|DECRETO[^ N�]{3})([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal�cio do Campo das Princesas)";
		String regexCorpo4 = "(DECRETA|DECRETO[^ N�]{3})([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal�cio do Campo das Princesas[A-Za-z������������������������ ,�0-9\\.]+(ANEXO))";
		String regexCorpo2 = "(DECRETA|DECRETO[^ N�]{3})([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexCorpo3 = "(Art)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;,���.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal�cio do Campo das Princesas)";
		String regexFinalmentes = "(O )?(Pal�cio do Campo das Princesas)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;,���.<>-_\\\\E \n\t\u00A7\u002D]+)(Independ�ncia do Brasil(\\.)?)";
		String regexAssinaturas = "([A-Z������������ \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exerc�cio)?([A-Z������������ \']+)";
		String regexAssinaturas2 = "([A-Z������������ \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exerc�cio)?([A-Z������������ \']+)(ANEXO)";
		String regexAnexos = "(NO )?(ANEXO)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAnexos2 = "(NO )?(ANEXO)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)(ERRATA [^PUBLI]+)";
		String regexErrata = "(ERRATA)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexInfo = "(\\Q(ERRATA PUBLICADA NO DI�RIO OFICIAL DE \\E\\d{1,2} DE [A-Z�]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SA�DO COM INCORRE��O NO ORIGINAL)\\E)";

		//REMOVER O TANTO DE CARACTERES QUE TEM EM ASSINATURA
		String regexConsiderando = "<CONSIDERACOES>([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)</CONSIDERACOES>";
		String regexAssinatura = "<ASSINATURAS>([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)</ASSINATURAS>";
		String regexLeis = "( Decreto| \\QDecreto-Lei\\E| Decreto Lei| Decreto Lei Federal| Decreto-Lei Federal| Lei Federal| Lei Complementar| Lei| Resolu��o| Emenda Constitucional| Ad Referendum| Of�cio| Parecer| Parecer Conjunto| Portaria Conjunta| Portaria)([A-Z \\Q/-\\E]{0,16})( (n�|n�) (\\d{1,3}\\.\\d{3}|\\d{3}|\\d{2}|\\d{2,4}/\\d{2,4}[A-Z/ -]{0,10}),(( de (\\d{1,2}|1�|1�) de ([a-z�A-Z�]{4,9}))? de (\\d{4}))?)( da Constitui��o Estadual)?";
		String regexDinheiro = " R$ [0-9\\Q.\\E]{1,},\\d{2} ";
		String regexAnexo = "<ANEXOS>([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#=;/,���.<>-_\\\\E \n\t\u00A7\u002D]+)</ANEXOS>";//s� esse tem sinal igual

		//Padr�es e Matchers
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
		Pattern padraoDinheiro = Pattern.compile(regexDinheiro);
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
		tiposDocumentos.put("Resolu��o", "RE");
		tiposDocumentos.put("Emenda Constitucional", "EC");
		tiposDocumentos.put("Ad Referendum", "AR");
		tiposDocumentos.put("Of�cio", "OF");
		tiposDocumentos.put("Parecer", "PAR");
		tiposDocumentos.put("Parecer Conjunto", "PC");
		tiposDocumentos.put("Portaria", "Pt");
		tiposDocumentos.put("Portaria  Conjunta", "PtC");

		Hashtable<String, String> numeracaoMeses = new Hashtable<>();
		numeracaoMeses.put("janeiro", "01");
		numeracaoMeses.put("fevereiro", "02");
		numeracaoMeses.put("mar�o", "03");
		numeracaoMeses.put("abril", "04");
		numeracaoMeses.put("maio", "05");
		numeracaoMeses.put("junho", "06");
		numeracaoMeses.put("julho", "07");
		numeracaoMeses.put("agosto", "08");
		numeracaoMeses.put("setembro", "09");
		numeracaoMeses.put("outubro", "10");
		numeracaoMeses.put("novembro", "11");
		numeracaoMeses.put("dezembro", "12");

		Hashtable<String, Integer> quantidadeDecretosAnexos = new Hashtable<>();
		Hashtable<String, Integer> quantidadeAnexosTabela = new Hashtable<>();
		File arquivoRegexNaoCaptura = new File("regexNaoCaptura");
		try {
			arquivoRegexNaoCaptura.createNewFile();
			fwarq = new FileWriter(arquivoRegexNaoCaptura);
			bwarq = new BufferedWriter(fwarq);
		} catch (IOException e1) {
			System.out.println("Arquivo n�o criado.");
		}

		String decretoIntermediario = "";
		String decretoSaida = "";
		String decreto;
		String linha;

		quantidadeDecretosAnexos.put("Abre", 0);
		quantidadeDecretosAnexos.put("Acrescenta", 0);
		quantidadeDecretosAnexos.put("Aloca", 0);
		quantidadeDecretosAnexos.put("Altera", 0);
		quantidadeDecretosAnexos.put("Amplia", 0);
		quantidadeDecretosAnexos.put("Aprova", 0);
		quantidadeDecretosAnexos.put("Autoriza", 0);
		quantidadeDecretosAnexos.put("Atualiza", 0);
		quantidadeDecretosAnexos.put("Ativa", 0);
		quantidadeDecretosAnexos.put("Concede", 0);
		quantidadeDecretosAnexos.put("Convoca", 0);
		quantidadeDecretosAnexos.put("Cria", 0);
		quantidadeDecretosAnexos.put("Declara", 0);
		quantidadeDecretosAnexos.put("Decreta", 0);
		quantidadeDecretosAnexos.put("Define", 0);
		quantidadeDecretosAnexos.put("Delega", 0);
		quantidadeDecretosAnexos.put("Desativa", 0);
		quantidadeDecretosAnexos.put("Disciplina", 0);
		quantidadeDecretosAnexos.put("Disp�e", 0);
		quantidadeDecretosAnexos.put("Eleva", 0);
		quantidadeDecretosAnexos.put("Estabelece", 0);
		quantidadeDecretosAnexos.put("Estende", 0);
		quantidadeDecretosAnexos.put("Homologa", 0);
		quantidadeDecretosAnexos.put("Incorpora", 0);
		quantidadeDecretosAnexos.put("Institui", 0);
		quantidadeDecretosAnexos.put("Interpreta", 0);
		quantidadeDecretosAnexos.put("Introduz", 0);
		quantidadeDecretosAnexos.put("Modifica", 0);
		quantidadeDecretosAnexos.put("Promove", 0);
		quantidadeDecretosAnexos.put("Prorroga", 0);
		quantidadeDecretosAnexos.put("Qualifica", 0);
		quantidadeDecretosAnexos.put("Reabre", 0);
		quantidadeDecretosAnexos.put("Redenomina", 0);
		quantidadeDecretosAnexos.put("Regulamenta", 0);
		quantidadeDecretosAnexos.put("Relaciona", 0);
		quantidadeDecretosAnexos.put("Renova", 0);
		quantidadeDecretosAnexos.put("Revoga", 0);
		quantidadeDecretosAnexos.put("Transfere", 0);
		quantidadeDecretosAnexos.put("Transforma", 0);

		quantidadeAnexosTabela.put("Abre", 0);
		quantidadeAnexosTabela.put("Acrescenta", 0);
		quantidadeAnexosTabela.put("Aloca", 0);
		quantidadeAnexosTabela.put("Altera", 0);
		quantidadeAnexosTabela.put("Amplia", 0);
		quantidadeAnexosTabela.put("Aprova", 0);
		quantidadeAnexosTabela.put("Autoriza", 0);
		quantidadeAnexosTabela.put("Atualiza", 0);
		quantidadeAnexosTabela.put("Ativa", 0);
		quantidadeAnexosTabela.put("Concede", 0);
		quantidadeAnexosTabela.put("Convoca", 0);
		quantidadeAnexosTabela.put("Cria", 0);
		quantidadeAnexosTabela.put("Declara", 0);
		quantidadeAnexosTabela.put("Decreta", 0);
		quantidadeAnexosTabela.put("Define", 0);
		quantidadeAnexosTabela.put("Delega", 0);
		quantidadeAnexosTabela.put("Desativa", 0);
		quantidadeAnexosTabela.put("Disciplina", 0);
		quantidadeAnexosTabela.put("Disp�e", 0);
		quantidadeAnexosTabela.put("Eleva", 0);
		quantidadeAnexosTabela.put("Estabelece", 0);
		quantidadeAnexosTabela.put("Estende", 0);
		quantidadeAnexosTabela.put("Homologa", 0);
		quantidadeAnexosTabela.put("Incorpora", 0);
		quantidadeAnexosTabela.put("Institui", 0);
		quantidadeAnexosTabela.put("Interpreta", 0);
		quantidadeAnexosTabela.put("Introduz", 0);
		quantidadeAnexosTabela.put("Modifica", 0);
		quantidadeAnexosTabela.put("Promove", 0);
		quantidadeAnexosTabela.put("Prorroga", 0);
		quantidadeAnexosTabela.put("Qualifica", 0);
		quantidadeAnexosTabela.put("Reabre", 0);
		quantidadeAnexosTabela.put("Redenomina", 0);
		quantidadeAnexosTabela.put("Regulamenta", 0);
		quantidadeAnexosTabela.put("Relaciona", 0);
		quantidadeAnexosTabela.put("Renova", 0);
		quantidadeAnexosTabela.put("Revoga", 0);
		quantidadeAnexosTabela.put("Transfere", 0);
		quantidadeAnexosTabela.put("Transforma", 0);

		String tipo = "";
		//AQUI COME�A A REMO��O DA FORMATA��O

		for (int i = 0; i < anosDecretos.length; i++){
			File a = new File("decretos" + anosDecretos[i]);
			File b = new File("anexos" + anosDecretos[i]);
			File c = new File("tabelas" + anosDecretos[i]);

			try {
				a.createNewFile();
				b.createNewFile();
				c.createNewFile();
			} catch (IOException e1) {
				System.out.println("Os arquivos est�o defeituosos.");
			}


			arquivoPasta = new File(caminhoDecretos + anosDecretos[i] + "\\" + pastaDestino);
			System.out.println(arquivoPasta);
			if (!arquivoPasta.exists()){ //Se pasta n�o existe, cria
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
			contadorTiposDecreto.put("Disp�e", 0);
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
				nomeArquivo = (arquivos[i][j]).getName(); //obt�m o nome do arquivo
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
						tipo = matcherAuxiliar.group(1);
						contadorTiposDecreto.containsKey(matcherAuxiliar.group(1));
						contadorTiposDecreto.put(matcherAuxiliar.group(1), contadorTiposDecreto.get(matcherAuxiliar.group(1)) + 1);
					} else if (matcher.find()){
						decretoSaida += "<DESCRICAO>" +
								matcher.group(1) + " " + matcher.group(2) + "<";
						tipo = matcher.group(1);
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
					} else if (decretoIntermediario.contains("Pal�cio do Campo")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "FINALMENTES");
						bwarq.newLine();
					}

					matcher = padraoAssinaturas.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAssinaturas2.matcher(decretoIntermediario);

					if (matcherAuxiliar.find()){
						decretoSaida += "<ASSINATURAS>" + matcherAuxiliar.group(1).trim() + " " 
								+ matcherAuxiliar.group(2);
						if (matcherAuxiliar.group(3) != null) decretoSaida += " " + matcherAuxiliar.group(3);
						decretoSaida += "  " + matcherAuxiliar.group(4).replaceFirst("ERRATA[ A-Z������������]+","").trim() + "</ASSINATURAS>";
					} else if (matcher.find()){
						decretoSaida += "<ASSINATURAS>" + matcher.group(1).trim() + " "
								+ matcher.group(2);
						if (matcher.group(3) != null) decretoSaida += " " + matcher.group(3);
						decretoSaida +=  "  " + matcher.group(4).replaceFirst("ERRATA[ A-Z������������]+","").trim() + "</ASSINATURAS>";
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
								+ matcher.group(2).replaceFirst("(\\Q(ERRATA PUBLICADA NO DI�RIO OFICIAL DE \\E\\d{1,2} DE [A-Z�]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SA�DO COM INCORRE��O NO ORIGINAL)\\E)" , "").trim()
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
							textoModificado += "<ASSINATURA_OUTRO>" + segmentosConsAss[k].trim() + "</ASSINATURA_OUTRO>";
						}
						decretoSaida = decretoSaida.replace(entreTags, textoModificado);
					}

					matcher = padraoLeis.matcher(decretoSaida);

					while (matcher.find()){
						entreTags = matcher.group(0);
						//System.out.println(entreTags);
						textoModificado = " <" +  tiposDocumentos.get(matcher.group(1).trim())+ " numeracao=" + matcher.group(5);
						if (matcher.group(7) != null){
							textoModificado += " data=" + (matcher.group(8).length() < 2? "0" + matcher.group(8) : matcher.group(8))
									+ "-" + numeracaoMeses.get(matcher.group(9).toLowerCase()) + "-" + matcher.group(10);
						} else {
							textoModificado += " ano=" + matcher.group(10);
						}
						textoModificado+= ">" + matcher.group(0).trim() + "</" + tiposDocumentos.get(matcher.group(1).trim()) + ">";
						decretoSaida = decretoSaida.replace(entreTags, textoModificado);
					}

					matcher = padraoDinheiro.matcher(decretoSaida);

					while (matcher.find()){
						entreTags = matcher.group(0);
						//System.out.println(entreTags);
						textoModificado = " <DINDIN>" + matcher.group(0).trim() + "</DINDIN> ";
						decretoSaida = decretoSaida.replace(entreTags, textoModificado);
					}




					matcher = padraoAnexo.matcher(decretoSaida);
					textoModificado = "";
					//String[] tabelas;
					//int qtdTabelas = 0;
					if (matcher.find()){
						System.out.println(nomeArquivo);
						quantidadeDecretosAnexos.containsKey(tipo);
						quantidadeDecretosAnexos.put(tipo, quantidadeDecretosAnexos.get(tipo) + 1);
						entreTags = matcher.group(1);
						if (entreTags.startsWith("ANEXO �NICO")){
							//System.out.println(entreTags);
							if (entreTags.contains("MEMORIAL DESCRITIVO")){
								textoModificado = "<MEMO>" + entreTags + "</MEMO>";
								if (entreTags.contains("table")){
									quantidadeAnexosTabela.containsKey(tipo);
									quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);
								}
							} else if (entreTags.contains("table")){
								textoModificado = "<TABELAS>" + entreTags + "</TABELAS>";					
								quantidadeAnexosTabela.containsKey(tipo);
								quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);
							} else {
								textoModificado = "<OUTRO>" + entreTags + "</OUTRO>";
								if (entreTags.contains("table")){
									quantidadeAnexosTabela.containsKey(tipo);
									quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);
								}
							}
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
						} else {
							segmentosConsAss = entreTags.split("ANEXO [IVX]{1,3}");

							for (int k = 1; k < segmentosConsAss.length; k++){
								if (segmentosConsAss[k].contains("MEMORIAL DESCRITIVO")){
									textoModificado += "<MEMO>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</MEMO>";
								}else if (segmentosConsAss[k].contains("table")){
									textoModificado += "<TABELAS>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</TABELAS>";
									/*
									tabelas = segmentosConsAss[k].split("</table>");
									for (int l = 0; l < tabelas.length; l++){
										if (tabelas[l].contains("table")){
											qtdTabelas++;
										}
									}*/
									//System.out.println(nomeArquivo + "    " + qtdTabelas);
								} else{
									textoModificado += "<OUTRO>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</OUTRO>";
								}
							}
							if (entreTags.contains("table")){
								quantidadeAnexosTabela.containsKey(tipo);
								quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);
							}
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
						}
					}

					decretoSaida = decretoSaida.replaceAll("&middot;", "\u00B7");
					decretoSaida = decretoSaida.replaceAll("&sup2;","\u00B2");
					decretoSaida = decretoSaida.replaceAll(" m 2;"," m\u00B2");
					decretoSaida = decretoSaida.replaceAll("&sup3;","\u00B3");




					fw = new FileWriter(a);
					bw = new BufferedWriter(fw);

					fwb = new FileWriter(b);
					bwb = new BufferedWriter(fwb);

					fwc = new FileWriter(c);
					bwc = new BufferedWriter(fwc);
					List<String> contador = new ArrayList<>(contadorTiposDecreto.keySet());
					for (int v = 0; v < contador.size(); v++){	
						bw.write(contador.get(v) + "    " + contadorTiposDecreto.get(contador.get(v)));
						bw.newLine();

						bwb.write(contador.get(v) + "    " + quantidadeDecretosAnexos.get(contador.get(v)));
						bwb.newLine();

						bwc.write(contador.get(v) + "    " + quantidadeAnexosTabela.get(contador.get(v)));
						bwc.newLine();
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
			try {
				bwb.flush();
				bwb.close();
				bwc.flush();
				bwc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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