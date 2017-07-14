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

public class Segmentacao {

	private static Hashtable<String, Integer> quantidadeDecretosAnexos = new Hashtable<>();
	private static Hashtable<String, Integer> quantidadeAnexosTabela = new Hashtable<>();
	private static Hashtable<String, Integer> quantidadeTabelaOrcamentarias= new Hashtable<>();
	private static Map<String, Integer> contadorTiposDecreto = new Hashtable<>();
	private static Hashtable<String, Integer> quantidadeDecretosTabela = new Hashtable<>();
	private static Hashtable<String, Integer> quantidadeDecretosTabelaOrcamentaria = new Hashtable<>();
	private static Hashtable<String, Integer> somaDecretosTabela = new Hashtable<>();
	private static Hashtable<String, Integer> somaDecretosTabelaOrcamentaria = new Hashtable<>();

	private static Hashtable<String, Integer> nomePessoas = new Hashtable<>();

	static String nomeArquivo = "";
	public static void main(String[] args) {
		//Pastas de origem e destino
		String pastaDestino = "XMLizada";
		String pastaOrigem = "semFormataÁ„o";
		//Caminhos dos decretos
		//Substitua para a localizaÁ„o em seu computador
		String caminhoDecretos = "C:\\Users\\JN\\Documents\\DecretosAlepe\\";

		int[] anosDecretos = {2014, 2015, 2016};
		File b = new File("anexosAno");
		File c = new File ("tabelasAno");
		File d = new File("nomePessoas");
		File f = new File("tabelaOrcamentariaAno");
		File g = new File("qtdDecretosComTabela");
		File h = new File("qtdDecretosComTabelaOrcamentaria");
		File y = new File("somaDecretosComTabela");
		File z = new File("somaDecretosComTabelaOrcamentaria");

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
		FileWriter fwd;
		BufferedWriter bwd = null;
		FileWriter fwf;
		BufferedWriter bwf = null;
		FileWriter fwg;
		BufferedWriter bwg = null;
		FileWriter fwh;
		BufferedWriter bwh = null;
		FileWriter fwi;
		BufferedWriter bwi = null;
		FileWriter fwj;
		BufferedWriter bwj = null;
		FileWriter fwarq;
		BufferedWriter bwarq = null;

		File arquivoPasta;

		//Regex
		//regex de seÁ„o
		String regexIdentificacao = "(DECRETO N∫)\\s+((\\d{2}.\\d{3})(,|.)? (DE|DIA) (\\d{1,2})(∞|∫)? DE ([A-Z«]+) DE (\\d{4}).)";
		String regexTipoDecreto = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Dispıe|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco)";
		String regexTipoDecreto2 = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Dispıe|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERCÕCIO DO CARGO DE GOVERNADOR DO ESTADO)";
		String regexAtribuicoes = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(CONSIDERANDO|DECRETA|DECRETO)";
		String regexAtribuicoes2 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAtribuicoes3 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Art)";
		String regexConsideracoes = "(CONSIDERANDO)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,ß∫∞™./<>-_\\\\E \n\t\u00A7\u002D]+)(DECRETA)";
		String regexCorpo = "(DECRETA|DECRETO[^ N∞]{3})([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal·cio( do)? Campo das Princesas)";
		String regexCorpo4 = "(DECRETA|DECRETO[^ N∞]{3})([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal·cio( do)? Campo das Princesas[A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄« ,∫0-9\\.]+(ANEXO))";
		String regexCorpo2 = "(DECRETA|DECRETO[^ N∞]{3})([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexCorpo3 = "(Art)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal·cio( do)? Campo das Princesas)";
		String regexFinalmentes = "(O )?(Pal·cio( do)? Campo das Princesas)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(IndependÍncia do Brasil(\\.)?)";
		String regexAssinaturas = "([A-Z¬√¡¿… Õ”‘’⁄« \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exercÌcio)?([A-Z¬√¡¿… Õ”‘’⁄« \']+)";
		String regexAssinaturas2 = "([A-Z¬√¡¿… Õ”‘’⁄« \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exercÌcio)?([A-Z¬√¡¿… Õ”‘’⁄« \']+)(ANEXO)";
		String regexAnexos = "(NO )?(ANEXO)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q[]()*!?&$ß%:@#+;=/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAnexos2 = "(NO )?(ANEXO)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q[]()*!?&$ß%:@+#;=/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(ERRATA [^PUBLI]+)";
		String regexErrata = "(ERRATA)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;=/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexInfo = "(\\Q(ERRATA PUBLICADA NO DI¡RIO OFICIAL DE \\E\\d{1,2} DE [A-Z«]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SAÕDO COM INCORRE«√O NO ORIGINAL)\\E|\\Q(Revogado\\E [0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]{5,70}\\)|\\Q(Vide errata no final do texto.)\\E)";

		//regex de conserto
		String regexConsiderando = "<CONSS>([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;=/,∫∞™.<>-_\\\\E \n\t\u00A7]+)</CONSS>";
		String regexAssinatura = "<ASS>([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\'\\Q()=,.<>-\\\\E \n\t]+)</ASS>";
		String regexAnexo = "<ANEXOS>([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q[]()!+?*&$ß%:@#=;/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)</ANEXOS>";//sÛ esse tem sinal igual

		//regex de captura
		String regexLeis = "(( |\"|\\(|>)(Decreto|\\QDecreto-Lei\\E|Decreto Lei|Decreto Lei Federal|\\QDecreto-Lei\\E Federal|Lei Federal|Lei Complementar|Lei Complementar Federal|Lei|ResoluÁ„o|Emenda Constitucional|Ad Referendum|OfÌcio|Parecer|Parecer Conjunto|Portaria Conjunta|Portaria|Ato|Ato DeclaratÛrio|Termo|Ajuste|Ata|AcÛrd„o|Protocolo|Lei OrÁament·ria Anual)( do| da)?[A-Z \\Q/-.\\E]{0,16}(( n∫| n∞)? (\\d{1,2}\\.\\d{3}|\\d{3}|\\d{2}|\\d{2,4}/\\d{2,4}[A-Z\\Q/ -\\E]{0,10}))?(,)?((( de| em)? (\\d{1,2})(∞|∫)?( de |[\\Q./-\\E])([a-zÁA-Z«]{4,9}|\\d{1,2}[\\Q./-\\E]))?( de )?(\\d{4}))?)(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù0-9])";
		//String regexDoc = "( |\"|\\()(AudiÍncia(s)?|Manua(l|is)|Caderno(s)?|CÛdigo(s)?|ConstituiÁ(„o|ıes)|Estatuto(s)?|Termo(s)?|Documento(s)?|Cadastro(s)?|Quadro(s)?|DeclaraÁ(„o|ıes)|Certificado(s)?|Regimento(s)?|Regulamento(s)?|Termo(s)?|Orde(m|ns) Banc·ria(s)?|Registro(s)?|Contrato(s)?|BalanÁo(s)?|Minuta(s)?|Balancete(s)?|Nota(s)?|RelatÛrio(s)?|ResoluÁ(„o|ıes)|Anexo(s)?|Memoria(l|is)|ClassificaÁ(„o|ıes)|ConvÍnio(s)?) (((de |do |dos |da |das |‡ |‡s |ao |aos |pelo |pelos |pelas |pela |por |em |para |com |com os |com as )?(<(INST|GRUP|UNI|SECR|ED)>)*([A-Z¬¡… Õ”‘⁄]|[0-9])([A-Z¬√¡… Õ”‘’⁄«]*|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+|[0-9]+)(, |-| (e )?)?)+(\\s*(-|/|\\()\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&-\\E]+(\\))?)*(</(INST|GRUP|UNI|SECR|ED)>)*(( )?(n∫ )?[0-9\\Q/-\\E]{2,})?\\s*(, |<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù0-9]( )?))+";
		String regexDoc = "( |\"|\\()(AudiÍncia(s)?|Manua(l|is)|Caderno(s)?|CÛdigo(s)?|ConstituiÁ(„o|ıes)|Estatuto(s)?|Termo(s)?|Documento(s)?|Cadastro(s)?|Quadro(s)?|DeclaraÁ(„o|ıes)|Certificado(s)?|Regimento(s)?|Regulamento(s)?|Termo(s)?|Orde(m|ns) Banc·ria(s)?|Registro(s)?|Contrato(s)?|BalanÁo(s)?|Minuta(s)?|Balancete(s)?|Nota(s)?|RelatÛrio(s)?|ResoluÁ(„o|ıes)|Anexo(s)?|Memoria(l|is)|ClassificaÁ(„o|ıes)|ConvÍnio(s)?) (((de |do |dos |da |das |‡ |‡s |ao |aos |pelo |pelos |pelas |pela |por |em |para |com |com os |com as )?([A-Z¬¡… Õ”‘⁄]|[0-9])([A-Z¬√¡… Õ”‘’⁄«]*|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+|[0-9]+)(, |-| (e )?)?)+(\\s*(-|/|\\()\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&-\\E]+(\\))?)*(</(INST|GRUP|UNI|SECR|ED)>)*(( )?(n∫ )?[0-9\\Q/-\\E]{2,})?\\s*(, |<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù0-9]( )?))+";

		String regexDoc2 = " OrÁamento Fiscal( do Estado| da Uni„o)?| OrÁamento de Investimento( das Empresas)?| Di·rio Oficial do Estado(\\s*-\\s*DOE)?| Di·rio Oficial da Uni„o(\\s*-\\s*DOU)?"; 
		String regexDoc3 = "( |\"|\\(|>)(Anexo(s)?)\\s*[0-9]{1,2}\\s*\\-([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄« ,]+)(<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E)";
		String regexDinheiro = " R\\$\\s+[0-9.]+,\\d{2}";
		String regexPorcentagem = " [0-9]+(,\\d{1,})?( )?%";
		String regexEmpresaLTDA = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|antiga|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)?( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{1,3})+(\\QLTDA.\\E|LTDA)( ME)?(\\s*(-| |/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*))(,|\\Q.\\E|;|:|\"| [a-zÈ‡·ÌÛ˙Í‚Ù0-9]|, [a-z]|\\)|\\Q(\\E|<)";
		String regexEmpresaSA = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|antiga|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)?( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{1,3})+(\\QS/A\\E|\\QS.A.\\E|\\QS.A\\E)(\\s*(-| |/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*))(,|\\Q.\\E|;|:|\"| [a-zÈ‡·ÌÛ˙Í‚Ù0-9]|\\)|\\Q(\\E|<)";
		String regexEmpresaNaoSALTDA = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{1,3})+(\\s*(-| |/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*))(,|\\Q.\\E|;|:|\"|\\)|<)";
		String regexEmpresaNaoSALTDASemSilga = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{0,3})+))(,|\\Q.\\E|;|:|\"| [a-z])";
		String regexInst = "(( |\"|\\(|>)(FundaÁ(„o|ıes)|EscritÛrio(s)?|OrganizaÁ(„o|ıes)|Instituto(s)?|Departamento(s)?|Procuradoria(s)?|AgÍncia(s)?|Junta(s)?|Assembleia(s)?|C‚mara(s)?|ConsÛrcio(s)?|Defensoria(s)?|Tribuna(l|is)|Companhia(s)?|ConservatÛrio(s)?|Controladoria(s)?|GerÍncia(s)?|Contadoria(s)?|InteligÍncia(s)?|Corpo(s)?|Centra(l|is)|Banco(s)?|PolÌcia(s)?|Diretoria(s)?|Receita|Delegacia(s)?|Autarquia(s)?|SuperintendÍncia(s)?|MinistÈrio(s)?|Centro(s)?) (((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/|\\()\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+(\\))?)*)\\s*(, |<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù0-9]))+";
		String regexInst2 = "( AD Diper| Adagro| Arquivo P˙blico Digital| Porto Digital| Porto de Recife| ProRural| Complexo Industrial Portu·rio( ([A-Z¡…Õ”⁄¬ ‘][A-Z¡…Õ”⁄¬ ‘√’«a-z·ÈÌÛ˙‚ÍÙÁ„ı]+( )?)+)?| Pernambuco ParticipaÁıes e Investimento S/A( - Perpart)?| ServiÁo de ProteÁ„o ao Consumidor( - PROCON)?| AdministraÁ„o Geral de Fernando de Noronha| Congresso Nacional| LaboratÛrio FarmacÍutico de Pernambuco(\\s*(-)?\\s*LAFEPE)?)";
		String regexGrupo = "(( |\"|\\(|>)(Comiss(„o|ıes)|ComitÍ(s)?|Grupo(s)?|Grupamento(s)?|Conselho(s)?|CoordenaÁ(„o|ıes)|Coordenadoria(s)?|Assessoria(s)?|Sociedade(s)?) ((e )?(de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?(<ED>)?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| )?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)(</ED>)?\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexUnidade = "(( |\"|\\(|>)((?i)Unidade) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])"; 
		String regexSecretaria = "(( |\"|\\(|>)((?i)(Secretaria)|(?i)(Gabinete))(s)? (((de |do |dos |da |das |‡ |‡s |ao |aos |pelo |pelos |pelas |pela |por |em |para |com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(, |\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù<0-9]|<|\\Q-\\E))+";
		String regexSecretaria2 = "( Assessoria Especial| Casa Militar| Vice-Governadoria| Procuradoria Geral do Estado| LideranÁa do Governo na Assembleia Legislativa)";
		String regexServico2 = "( Delegacia Virtual| Certid„o Negativa| Bolsa de Empregos)";
		String regexServico = "(( |\"|\\(|>)ServiÁo ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexProgProj = "(( |\"|\\(|>)(Projeto(s)?|ConsolidaÁ(„o|ıes)|Programa(s)?|PolÌtica(s)?|Plano(s)?|Pacto(s)?) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?(<CAR>|<ORG>)?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?(</CAR>|</ORG>)?)+(\\s*(-|/| )\\s*([A-Z]|[0-9])([A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+|[0-9]+))*| Simples Nacional)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexRegime = "(( |\"|\\(|>)(Regime(s)?) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?(</CAR>|</ORG>)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexFundo = "(( |\"|\\(|>)(Fundo(s)?) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?(</CAR>|</ORG>)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexGratificacao = "(( |\"|\\(|>)(GratificaÁ(„o|ıes)|CompensaÁ(„o|ıes)|BonificaÁ(„o|ıes)|Abono(s)?|BenefÌcio(s)?) (e )?((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, | (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexPremio = "(( |\"|\\(|>)(PrÍmio(s)?|Medalha(s)?|MenÁ(„o|ıes)|Homenage(m|ns)|Honraria(s)?|CondecoraÁ(„o|ıes)|L·urea(s)?) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para (o |a )?|com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexEdificacao = "(( |\"|\\(|>)(PrÈdio(s)?|EdifÌcio(s)?|Escola(s)?|Pal·cio(s)?|PresÌdio(s)?|Arena(s)?|Refinaria(s)?|Estaleiro(s)?|Polo(s)?|EstaÁ(„o|ıes)|Coletor(es|a|as)?|ReservatÛrio(s)?|CartÛrio(s)?|Universidade(s)?|Hospita(l|is)( Regina(l|is)| Federa(l|is)| Estadua(l|is)| Municipa(l|is))?) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(-| (e )?)?)+(\\s*(-|/|\\Q(\\E)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+(\\Q)\\E)?)*\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z„‡·‚ÈÍÌÛÙı˙]))";
		String regexSistema = "(( |\"|\\(|>)(Sistema|sistema) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?(\\Qe-\\E)?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/|\\()\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexEvento = "(( |\"|\\(|>)([IVX]{1,5} |\\d{1,3}(∫|∞|™) )?(ConferÍncia|Congresso|Evento|Show|Concerto|SimpÛsio|Debate|FÛrum|Estudo|Copa) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(\\. |, | - | (e )?)?)+((de )?\\d{4})?(\\s*(-|/|\\()\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexPartido = "(( |\"|\\(|>)((?i)Partido(s)?) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |para |com |com os |com as )?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexData = "( (\\d{1,2})(∞|∫)? de ([a-zÁA-Z«]{4,9}) (do ano )?(de )?(\\d{4})| \\d{1,2}[\\Q./\\E]\\d{1,2}[\\Q./\\E]\\d{2,4})";
		String regexIntervaloData = " <DAT>[0-9A-Za-z \\Q∫∞/.\\E]+</DAT> a <DAT>[0-9A-Za-z \\Q∫∞/.\\E]+</DAT>| \\d{1,2}(∞|∫)?( de)? ([ A-Za-z]{4,10})? (a|e) <DAT>[0-9A-Za-z \\Q∫∞./\\E]+</DAT>| [A-Z«a-zÁ]{4,9} a [A-Z«a-zÁ]{4,9} de \\d{4}| \\d{4} a \\d{4}";
		String regexInfoEmp = " (estabelecid|situad|localizad)(a na|o no)( [0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\\Q() ,∫∞™-.;/'\"\\E]+ CNPJ(/MF)? n(∫|∞) \\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2} e CACEPE n(∞|∫) \\d{7}\\-\\d{2})";
		String regexCNPJ = " CNPJ(/MF)?( n∞| n∫)?( \\d{2}\\.\\d{3}\\.\\d{3}(/\\d{4}-\\d{2})?)";
		String regexIE = " (InscriÁ„o Estadual|IE)( n∞| n∫)?( \\d{1,3}\\.\\d{3}\\.\\d{3}(\\-\\d{1,2}|\\.\\d{3})?)";
		String regexCACEPE = " \\d{7}\\-\\d{2}";
		String regexMunicipio = "( ((M|m)unicÌpio|(A|a)rquipÈlago|(I|i)lha|(C|c)idade)(s)?( de| do| dos| da| das)?(:)?)(( [A-Z¬¡… Õ”‘⁄][A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡… Õ”‘’⁄« ]+(,|\\Q.\\E| e|;))+)";
		String regexCargo = "([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«]+)?(( | \"| \\(| >)(ex|EX|Ex)?(\\Q-\\E)?(Primeiro |Segundo |Terceiro |(1|2|3)(∫|∞|™) )?(Auditor(es)?|Comandante(s)?( Geral)?|Chefe(s)?|Sargent(o|a)(s)?|Tenente(s)?(-)?|Subtenente(s)?|Corone(l|Èis)|Capit„(o)?(s)?|Major(es)?|Fiscal|Educador|Cirurgi„o|Secret·ri(o|a)(s)?|Diretor(es|a|as)?(-)?|Agente(s)?|Ministr(o|a)(s)?|Deputad(o|a)(s)?|PresidÍncia|Presidente|Gerente(s)?|Assistente(s)?|Assessor(a|es|as)?|Superintendente(s)?|Coordenador(a|as|es)?|Gestor(es|a|as)?|TÈcnico(s)?|MÈdic(o|a)(s)?|Terapeuta(s)?|FarmacÍutic(o|a)(s)?|Condutor(es)?)(\\s*(destinad(o|a)(s)? |sobre |relativ(o|a)(s)? |com |para )?\\s*(de |do |dos |da |das |‡ |‡s |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na )?(<ORG>)?\\s*[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(</ORG>)?(, |\\s*-\\s*|\\s+(e )?)?)+\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù]))+";
		String regexCargo2 = "([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«]+)?(( | \"| >| /)(ex|EX|Ex)?(\\Q-\\E)?(Fisioterapeuta|Arte(\\s+|-)Educador(a|as|es)?|Delegad(o|a)(s)?|Escriv(„o|„es)|Agente(s)?|Governador(a|as|es)?( do Estado)?|Psicopedagogo|Arquiteto|Aeroportu·rio|MÈdico|Terapeuta|Jornalista|TurismÛlogo|Enfermeiro|PsicÛlogo|Bibliotec·rio|Arquivista|Professor))(,| [a-z ]{1,8}[^A-Z]| [A-Z]{3}|\\.|/|\\s*<)";
		String regexEncargo = "(( |\"|\\(|>)(Taxa(s)?|Imposto(s)?|Tributo(s)?|ContribuiÁ(„o|ıes)|EmprÈstimo(s)? CompulsÛrio(s)?|Financiamento(s)?|Encargo(s)?|Despesa(s)?) ((destinad(o|a)(s)? |sobre |relativ(o|a)(s)? |com |para )?(de |do |dos |da |das |‡ |‡s |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexIndice = "(( |\"|\\(|>)(Õndice) (((destinad(o|a)(s)? |sobre |relativ(o|a)(s)? |com |para )?(de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?))+(\\s*(-|/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexLocal = "( |\"|\\(|>)((SÌtio(s)?|Monumento(s)?|Riacho(s)?|Parque(s)|PraÁa(s)?|Serra(s)?|Mata(s)?|Floresta(s)?|Planalto(s)?|Depress(„o|ıes)) ((de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(, |-| (e )?)?|[0-9 e]+)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-zÈ‡·ÌÛ˙Í‚Ù])";
		String regexSite = " www(\\d)?((\\.[a-z]+))+(/( )?([A-Za-z]+)?)?";
		String regexEnd = " <INF_EMP>([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\\Q() ,∫∞™-.;/'\"\\E]+), com CNPJ(/MF)?";

		//Padrıes e Matchers
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
		Pattern padraoDoc = Pattern.compile(regexDoc);
		Pattern padraoDoc2 = Pattern.compile(regexDoc2);
		Pattern padraoDoc3 = Pattern.compile(regexDoc3);
		Pattern padraoDinheiro = Pattern.compile(regexDinheiro);
		Pattern padraoPorcentagem = Pattern.compile(regexPorcentagem);
		Pattern padraoEmpresaLTDA = Pattern.compile(regexEmpresaLTDA);
		Pattern padraoEmpresaSA = Pattern.compile(regexEmpresaSA);
		Pattern padraoEmpresaNaoSALTDA = Pattern.compile(regexEmpresaNaoSALTDA);
		Pattern padraoEmpresaNaoSALTDASemSigla = Pattern.compile(regexEmpresaNaoSALTDASemSilga);
		Pattern padraoInst = Pattern.compile(regexInst);
		Pattern padraoInst2 = Pattern.compile(regexInst2);
		Pattern padraoSecretaria = Pattern.compile(regexSecretaria);
		Pattern padraoSecretaria2 = Pattern.compile(regexSecretaria2);
		Pattern padraoGrupo = Pattern.compile(regexGrupo);
		Pattern padraoUnidade = Pattern.compile(regexUnidade);
		Pattern padraoServico = Pattern.compile(regexServico);
		Pattern padraoServico2 = Pattern.compile(regexServico2);
		Pattern padraoProgProj = Pattern.compile(regexProgProj);
		Pattern padraoSistema = Pattern.compile(regexSistema);
		Pattern padraoEvento = Pattern.compile(regexEvento);
		Pattern padraoData = Pattern.compile(regexData);
		Pattern padraoIntervaloData = Pattern.compile(regexIntervaloData);
		Pattern padraoInfoEmp = Pattern.compile(regexInfoEmp);
		Pattern padraoCNPJ = Pattern.compile(regexCNPJ);
		Pattern padraoIE = Pattern.compile(regexIE);
		Pattern padraoCACEPE = Pattern.compile(regexCACEPE);
		Pattern padraoMunicipio = Pattern.compile(regexMunicipio);
		Pattern padraoLocal = Pattern.compile(regexLocal);
		Pattern padraoCargo = Pattern.compile(regexCargo);
		Pattern padraoCargo2 = Pattern.compile(regexCargo2);
		Pattern padraoEncargo = Pattern.compile(regexEncargo);
		Pattern padraoIndice = Pattern.compile(regexIndice);
		Pattern padraoEnd = Pattern.compile(regexEnd);
		Pattern padraoSite = Pattern.compile(regexSite);
		Pattern padraoRegime = Pattern.compile(regexRegime);
		Pattern padraoPartido = Pattern.compile(regexPartido);
		Pattern padraoFundo = Pattern.compile(regexFundo);
		Pattern padraoGratificacao = Pattern.compile(regexGratificacao);
		Pattern padraoPremio = Pattern.compile(regexPremio);
		Pattern padraoEdificacao = Pattern.compile(regexEdificacao);
		Pattern padraoAnexo = Pattern.compile(regexAnexo);

		Matcher matcher;
		Matcher matcherAuxiliar;

		popularAsHashtablesSoma();

		Hashtable<String, String> tiposDocumentos = new Hashtable<>();
		tiposDocumentos.put("Lei", "LE");
		tiposDocumentos.put("Lei Complementar", "LC");
		tiposDocumentos.put("Lei Federal", "LF");
		tiposDocumentos.put("Decreto", "DE");
		tiposDocumentos.put("Decreto-Lei", "DL");
		tiposDocumentos.put("Decreto Lei", "DL");
		tiposDocumentos.put("Decreto-Lei Federal", "DLF");
		tiposDocumentos.put("Decreto Lei Federal", "DLF");
		tiposDocumentos.put("ResoluÁ„o", "Re");
		tiposDocumentos.put("Emenda Constitucional", "EC");
		tiposDocumentos.put("Ad Referendum", "AR");
		tiposDocumentos.put("OfÌcio", "OF");
		tiposDocumentos.put("Parecer", "PAR");
		tiposDocumentos.put("Parecer Conjunto", "PC");
		tiposDocumentos.put("Portaria", "Pt");
		tiposDocumentos.put("Portaria Conjunta", "PtC");
		tiposDocumentos.put("Ato", "At");
		tiposDocumentos.put("Ato DeclaratÛrio", "AD");
		tiposDocumentos.put("Ata", "ATA");
		tiposDocumentos.put("Ajuste", "Aj");
		tiposDocumentos.put("Termo", "Te");
		tiposDocumentos.put("Lei Complementar Federal", "LCF");
		tiposDocumentos.put("Protocolo", "PROT");
		tiposDocumentos.put("Lei OrÁament·ria Anual", "LOA");
		tiposDocumentos.put("ConvÍnio", "CONV");

		Hashtable<String, String> numeracaoMeses = new Hashtable<>();
		numeracaoMeses.put("janeiro", "01");
		numeracaoMeses.put("fevereiro", "02");
		numeracaoMeses.put("marÁo", "03");
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
			fwb = new FileWriter(b);
			bwb = new BufferedWriter(fwb);
			fwc = new FileWriter(c);
			bwc = new BufferedWriter(fwc);
			fwd = new FileWriter(d);
			bwd = new BufferedWriter(fwd);
			fwf = new FileWriter(f);
			bwf = new BufferedWriter(fwf);
			fwg = new FileWriter(g);
			bwg = new BufferedWriter(fwg);
			fwh = new FileWriter(h);
			bwh = new BufferedWriter(fwh);
			fwi = new FileWriter(y);
			bwi = new BufferedWriter(fwi);
			fwj = new FileWriter(z);
			bwj = new BufferedWriter(fwj);
			bwd.write("\nDados\n");
			bwd.newLine();
		} catch (IOException e1) {
			System.out.println("Arquivo n„o criado.");
		}

		String decretoIntermediario = "";
		String decretoSaida = "";
		String decreto;
		String linha;
		String tipo = "";
		String nomePessoa;
		String entreTags;

		String textoModificado = null;
		String[] segmentosConsAss;

		//AQUI COME«A A REMO«√O DA FORMATA«√O
		for (int i = 0; i < anosDecretos.length; i++){
			File a = new File("decretos" + anosDecretos[i]);
			try {
				bwb.write("\nDados para o ano de " + anosDecretos[i]);
				bwb.newLine();
				bwc.write("\nDados para o ano de " + anosDecretos[i]);
				bwc.newLine();
				bwf.write("\nDados para o ano de " + anosDecretos[i]);
				bwf.newLine();
				bwg.write("\nDados para o ano de " + anosDecretos[i]);
				bwg.newLine();
				bwh.write("\nDados para o ano de " + anosDecretos[i]);
				bwh.newLine();
				bwi.write("\nDados para o ano de " + anosDecretos[i]);
				bwi.newLine();
				bwj.write("\nDados para o ano de " + anosDecretos[i]);
				bwj.newLine();
			} catch (IOException e1) {
				System.out.println("N„o conseguiu escrever no documento");
			}

			arquivoPasta = new File(caminhoDecretos + anosDecretos[i] + "\\" + pastaDestino);
			//System.out.println(arquivoPasta);
			if (!arquivoPasta.exists()){ //Se pasta n„o existe, cria
				arquivoPasta.mkdir();
			}

			popularAsHashtables();

			for (int j = 0; j < arquivos[i].length; j++){
				nomeArquivo = (arquivos[i][j]).getName(); //obtÈm o nome do arquivo
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
					decretoSaida = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
					decretoSaida+= "<DECR>\n";

					matcher = padraoInfo.matcher(decretoIntermediario);
					while (matcher.find()){
						decretoSaida += "<INFO>" + matcher.group(0) + "</INFO>\n";
						decretoIntermediario = decretoIntermediario.replaceFirst("\\Q"+ matcher.group(0)+ "\\E", "");
					}

					matcher = padraoIdentificacao.matcher(decretoIntermediario);
					matcherAuxiliar = null;
					if (matcher.find()){
						decretoSaida += "<ID numeracao=\'" + matcher.group(3) + "\' data=\'" + (matcher.group(6).length() < 2? "0" + matcher.group(6) : matcher.group(6)) + "-" + numeracaoMeses.get(matcher.group(8).toLowerCase())
						+ "-" + matcher.group(9) + "\'>" + matcher.group(1).trim() + " " + matcher.group(2) + "</ID>\n<CORPO>\n";
						decretoIntermediario = decretoIntermediario.replace(matcher.group(0), "");
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "IDENTIFICACAO");
						bwarq.newLine();
					}

					matcher = padraoTipoDecreto.matcher(decretoIntermediario);
					matcherAuxiliar = padraoTipoDecreto2.matcher(decretoIntermediario);
					if (matcherAuxiliar.find()){
						/*tanto em cima quanto em baixo no decretoSaida+= tinha 
						 * concatenaÁ„o ocm "<"*/
						entreTags = matcherAuxiliar.group(1) + " " + matcherAuxiliar.group(2);
						decretoSaida += "<EM>" + entreTags;
						tipo = matcherAuxiliar.group(1);
						contadorTiposDecreto.put(matcherAuxiliar.group(1), contadorTiposDecreto.get(matcherAuxiliar.group(1)) + 1);

						decretoIntermediario = decretoIntermediario.replace(entreTags, "");
					} else if (matcher.find()){
						entreTags = matcher.group(1) + matcher.group(2);
						decretoSaida += "<EM>" + entreTags;
						tipo = matcher.group(1);
						contadorTiposDecreto.put(matcher.group(1), contadorTiposDecreto.get(matcher.group(1)) + 1);


						decretoIntermediario = decretoIntermediario.replace(entreTags, "");
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "EMENTA");
						bwarq.newLine();
					}


					String[] partes = decretoSaida.split("GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO");
					decretoSaida = partes[0].trim();
					decretoSaida += "</EM>\n";
					if (nomeArquivo.contains("41.416")){
						System.out.println(decretoSaida);
					}


					decretoSaida = decretoSaida.replace("<DECR>", "<DECR tipo=\'" + tipo.toLowerCase() + "\'>");

					matcher = padraoAtribuicoes.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAtribuicoes2.matcher(decretoIntermediario);
					decretoSaida += "<ATRIB>";
					if (matcher.find()){
						if (matcher.group(1) != null)
							decretoSaida += matcher.group(1).trim() + " ";
						decretoSaida += matcher.group(2).trim() + matcher.group(5).trim();
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
					decretoSaida += "</ATRIB>\n";
					//antes tinha <</EM>
					decretoSaida = decretoSaida.replaceFirst("\\s*O\\s*</EM>", "</EM>");

					matcher = padraoConsideracoes.matcher(decretoIntermediario);
					matcherAuxiliar = null;
					if (matcher.find()){
						decretoSaida += "<CONSS>\n" + matcher.group(1).trim() + " " 
								+ matcher.group(2).trim() + "</CONSS>\n";
					} else if (decretoIntermediario.contains("CONSIDERANDO")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "CONSIDERACOES");
						bwarq.newLine();
					}

					matcher = padraoCorpo4.matcher(decretoIntermediario);
					matcherAuxiliar = padraoCorpo.matcher(decretoIntermediario);
					if (matcher.find()){
						decretoSaida += "<ORD>" + matcher.group(1).trim() + " "
								+ matcher.group(2).trim() + "</ORD>\n";
					} else if (matcherAuxiliar.find()){
						decretoSaida += "<ORD>" + matcherAuxiliar.group(1).trim() + " "
								+ matcherAuxiliar.group(2).trim() + "</ORD>\n";
					} else{
						matcher = padraoCorpo2.matcher(decretoIntermediario);
						matcherAuxiliar = padraoCorpo3.matcher(decretoIntermediario);
						if (matcher.find()) {
							decretoSaida += "<ORD>" + matcher.group(1).trim() + " "
									+ matcher.group(2).trim() + "</ORD>\n";
						} else if (matcherAuxiliar.find()){
							decretoSaida += "<ORD>" + matcherAuxiliar.group(1).trim() + " "
									+ matcherAuxiliar.group(2).trim() + "</ORD>\n";	
						}else{
							bwarq.write(i + "     " + nomeArquivo + "     " + "ORDENAMENTO");
							bwarq.newLine();
						}
					}

					decretoSaida+= "</CORPO>\n";

					matcher = padraoFinalmentes.matcher(decretoIntermediario);
					matcherAuxiliar = null;
					if (matcher.find()){
						decretoSaida += "<FIN>";
						if (matcher.group(1) != null) decretoSaida +=  matcher.group(1).trim();
						decretoSaida += matcher.group(2).trim() + matcher.group(4).trim() + " " +
								matcher.group(5).trim() +  "</FIN>\n";
					} else if (decretoIntermediario.contains("Pal·cio do Campo")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "FINALMENTES");
						bwarq.newLine();
					}

					matcher = padraoAssinaturas.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAssinaturas2.matcher(decretoIntermediario);
					if (matcherAuxiliar.find()){
						decretoSaida += "<ASS>" + matcherAuxiliar.group(1).trim() + " " 
								+ matcherAuxiliar.group(2);
						if (matcherAuxiliar.group(3) != null) decretoSaida += " " + matcherAuxiliar.group(3);
						decretoSaida += "  " + matcherAuxiliar.group(4).replaceFirst("ERRATA[ A-Z¬√¡¿… Õ”‘’⁄«]+","").trim() + "</ASS>\n";
					} else if (matcher.find()){
						decretoSaida += "<ASS>" + matcher.group(1).trim() + " "
								+ matcher.group(2);
						if (matcher.group(3) != null) decretoSaida += " " + matcher.group(3);
						decretoSaida +=  "  " + matcher.group(4).replaceFirst("ERRATA[ A-Z¬√¡¿… Õ”‘’⁄«]+","").trim() + "</ASS>\n";
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "ASSINATURAS");
						bwarq.newLine();
					}

					matcher = padraoAnexos.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAnexos2.matcher(decretoIntermediario);
					if (matcherAuxiliar.find()){
						if(!matcherAuxiliar.group(0).startsWith("NO ")){
							decretoSaida += "<ANEXOS>" + matcherAuxiliar.group(2).trim() + " "
									+ matcherAuxiliar.group(3).trim() + "</ANEXOS>\n";
						}
					} else if (matcher.find()){
						if(!matcher.group(0).startsWith("NO ")){
							decretoSaida += "<ANEXOS>" + matcher.group(2).trim() + " "
									+ matcher.group(3).trim() + "</ANEXOS>\n";
						}
					} else if (decretoIntermediario.contains("ANEXO")){
						bwarq.write(i + "     " + nomeArquivo + "     " + "ANEXOS");
						bwarq.newLine();
					}

					matcher = padraoErrata.matcher(decretoIntermediario);
					matcherAuxiliar = null;
					if (matcher.find()){
						decretoSaida += "<ERR>" + matcher.group(1).trim() + " "
								+ matcher.group(2).replaceFirst("(\\Q(ERRATA PUBLICADA NO DI¡RIO OFICIAL DE \\E\\d{1,2} DE [A-Z«]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SAÕDO COM INCORRE«√O NO ORIGINAL)\\E)" , "").trim()
								+ "</ERR>\n";
					} 

					matcher = padraoConsiderando.matcher(decretoSaida);
					if (matcher.find()){
						entreTags = matcher.group(1);
						segmentosConsAss = entreTags.split("( )+CONSIDERANDO");
						textoModificado = "";

						for (int k = 0; k < segmentosConsAss.length; k++){
							if (k != 0)
								textoModificado += "<CONS>" + ("CONSIDERANDO" + segmentosConsAss[k]).trim() + "</CONS>\n";
							else
								textoModificado += "<CONS>" + segmentosConsAss[k].trim() + "</CONS>\n";
						}
						decretoSaida = decretoSaida.replace(entreTags, textoModificado);
					}

					matcher = padraoAssinatura.matcher(decretoSaida);
					if (matcher.find()){
						entreTags = matcher.group(1);
						segmentosConsAss = entreTags.split("( ){2,}");

						textoModificado = "";
						nomePessoa = segmentosConsAss[0].replaceFirst("Governador do Estado|GOVERNADOR DO ESTADO", "").trim();
						textoModificado += "<ASS_GOV>" + nomePessoa + "</ASS_GOV> " + segmentosConsAss[0].replaceFirst(nomePessoa, "");

						if (nomeArquivo.contains("43.133")){
							//System.out.println(entreTags + "   " + nomePessoa);
						}
						/*List<String> chavesPessoas = new ArrayList<>(nomePessoas.keySet());
							for (int p = 0; p < chavesPessoas.size(); p++){
								if (nomePessoa.contains(chavesPessoas.get(p))){
									nomePessoas.put(chavesPessoas.get(p), nomePessoas.get(chavesPessoas.get(p)) + 1);
								}
							}*/
						for (int k = 1; k < segmentosConsAss.length; k++){
							//System.out.println(segmentosConsAss[k]);
							nomePessoa = segmentosConsAss[k].trim();
							textoModificado += "\n<ASS_OUTRO>" + nomePessoa + "</ASS_OUTRO>";
							/*chavesPessoas = new ArrayList<>(nomePessoas.keySet());
							for (int p = 0; p < chavesPessoas.size(); p++){
								if (nomePessoa.contains(chavesPessoas.get(p))){
									nomePessoas.put(chavesPessoas.get(p), nomePessoas.get(chavesPessoas.get(p)) + 1);
								} else {
									nomePessoas.put(nomePessoa, 1);
								}
							}*/
						}
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					decretoSaida = decretoSaida.replace("table>", "TBL>");
					decretoSaida = decretoSaida.replaceAll("<(/)?tbody>", "");

					matcher = padraoAnexo.matcher(decretoSaida);
					if (matcher.find()){
						textoModificado = "";
						quantidadeDecretosAnexos.containsKey(tipo);
						quantidadeDecretosAnexos.put(tipo, quantidadeDecretosAnexos.get(tipo) + 1);

						entreTags = matcher.group(1);

						if (entreTags.startsWith("ANEXO ⁄NICO")){
							textoModificado = classificadorAnexos(entreTags, tipo);

							if (textoModificado.contains("ANULA«√O DE DOTA«√O")){
								textoModificado = tabelaReformulada(textoModificado, "dot");
							} else if (textoModificado.contains("CR…DITO SUPLEMENTAR")){
								textoModificado = tabelaReformulada(textoModificado, "supl");
							} else if (textoModificado.contains("EXCESSO DE ARRECADA«√O")){
								textoModificado = tabelaReformulada(textoModificado, "arr");
							}
							
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
							decretoSaida = decretoSaida.replace("<ANEXOS>", "<ANEXOS num_anexos=\'1\'>");
						} else {
							segmentosConsAss = entreTags.split("ANEXO [IVX]{1,4}");
							String modSegmentos = "";
							for (int k = 1; k < segmentosConsAss.length; k++){
								modSegmentos = classificadorAnexos(segmentosConsAss[k], tipo);
								if (modSegmentos != ""){
									if (modSegmentos.contains("ANULA«√O DE DOTA«√O")){
										modSegmentos = tabelaReformulada(modSegmentos, "dot");
									} else if (modSegmentos.contains("CR…DITO SUPLEMENTAR")){
										modSegmentos = tabelaReformulada(modSegmentos, "supl");
									} else if (modSegmentos.contains("EXCESSO DE ARRECADA«√O")){
										modSegmentos = tabelaReformulada(modSegmentos, "arr");
									}
								}
								textoModificado += modSegmentos;
							}
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
							decretoSaida = decretoSaida.replace("<ANEXOS>", "<ANEXOS num_anexos=\'" + (segmentosConsAss.length - 1) + "\'>");
						}
						
						///TAVA ANTES DO IF 
						if (entreTags.contains("TAB")){
							quantidadeDecretosTabela.containsKey(tipo);
							quantidadeDecretosTabela.put(tipo, quantidadeDecretosTabela.get(tipo) + 1);

							somaDecretosTabela.containsKey(tipo);
							somaDecretosTabela.put(tipo, somaDecretosTabela.get(tipo) + 1);
							
							if (entreTags.contains("CR…DITO SUPLEMENTAR") || 
									entreTags.contains("DOTA«√O")
									|| entreTags.contains("EXCESSO DE ARRECADA«√O")
									|| entreTags.contains("R$")){
								somaDecretosTabelaOrcamentaria.containsKey(tipo);
								somaDecretosTabelaOrcamentaria.put(tipo, somaDecretosTabelaOrcamentaria.get(tipo) + 1);
							} else {
								entreTags.replace("TBL", "TBL tipo=\'outro\'");
							}
						}
					}

					char primeiroCaractere;
					matcher = padraoLeis.matcher(decretoSaida);
					while (matcher.find() && (matcher.group(7) != null  || matcher.group(9) != null)){ //&& (matcher.group(6) != null || matcher.group(10) != null || matcher.group(11) != null)){
						entreTags = matcher.group(1);
						String tipoDoc = tiposDocumentos.get(matcher.group(3).trim());
						textoModificado = " <" +  tipoDoc;
						//	System.out.println("Leis1 " + entreTags);
						if (matcher.group(9) == null && matcher.group(7) != null){
							if (matcher.group(7).contains("/")){
								textoModificado += " numeracao=\'" + matcher.group(7).split("/")[0] + "\' ano=\'" + matcher.group(7).split("/")[1]  + "\'";
							} else{
								textoModificado += " numeracao=\'" + matcher.group(7)+ "\'";
							}
						} else if(matcher.group(7) == null && matcher.group(9) != null){
							textoModificado += " data=\'" + (matcher.group(12).length() < 2? "0" + matcher.group(12) : matcher.group(12))	+ "-";
							if  (matcher.group(14).contains(".") || matcher.group(14).contains("/") || matcher.group(14).contains("-")){
								textoModificado += (matcher.group(15).length() < 2? "0" + matcher.group(15): matcher.group(15)) + "-" + matcher.group(17);
							} else{
								textoModificado += numeracaoMeses.get(matcher.group(15).toLowerCase()) + "-" + matcher.group(17);
							}
							textoModificado += "\'";
						} else if (matcher.group(7) != null && matcher.group(15) != null){ // 12 È dia, 15 È mÍs, 17 ano
							textoModificado += " numeracao=\'" + matcher.group(7) + "\'";
							textoModificado += " data=\'" + (matcher.group(12).length() < 2? "0" + matcher.group(12) : matcher.group(12)) + "-";

							if  (matcher.group(14).contains(".") || matcher.group(14).contains("/") || matcher.group(14).contains("-")){
								textoModificado += (matcher.group(15).length() < 2? "0" + matcher.group(15): matcher.group(15)) + "-" + matcher.group(17);
							} else{
								textoModificado += numeracaoMeses.get(matcher.group(15).toLowerCase()) + "-" + matcher.group(17);
							}
							textoModificado += "\'";
						} else if (matcher.group(7) != null && matcher.group(17) != null){
							textoModificado += " numeracao=\'" + matcher.group(7) + "\'";
							textoModificado += " ano=\'" + matcher.group(17) + "\'";
						}

						textoModificado+= ">" + matcher.group(1).trim() + "</" + tipoDoc + ">";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, tipoDoc, "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q"+entreTags+"\\E", textoModificado);
					}

					matcher = padraoDinheiro.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(0);
						textoModificado = " <DIN>" + matcher.group(0).trim() + "</DIN> ";
						decretoSaida = decretoSaida.replace(entreTags, textoModificado);
					}

					matcher = padraoPorcentagem.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(0);
						textoModificado = " <PORC>" + matcher.group(0).trim() + "</PORC> ";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoEmpresaLTDA.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = "";
						if (matcher.group(3) == null){
							entreTags = matcher.group(3);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
						}
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "EMP", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

					}

					matcher = padraoEmpresaSA.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						if (matcher.group(3) == null){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "EMP", "", false);
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						}	
					}

					matcher = padraoEmpresaNaoSALTDA.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = "";
						if (matcher.group(3) == null){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
						}
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "EMP", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);	
					}

					matcher = padraoEmpresaNaoSALTDASemSigla.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = "";
						if (matcher.group(3) == null){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
						}
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "EMP", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoUnidade.matcher(decretoSaida);
					while (matcher.find()){
						//sem U mesmo
						if (!(matcher.group(1).contains("nidade Federativa")||
								matcher.group(1).contains("nidade da FederaÁ„o")||
								matcher.group(1).contains("nidade do MunicÌpio")||
								matcher.group(1).contains("nidade Municipal") ||
								matcher.group(1).contains("Unidade Escolar"))){
							entreTags = matcher.group(1);
							primeiroCaractere = entreTags.charAt(0);
							entreTags = entreTags.substring(1);

							/*Para identificar se uma sigla faz parte do seleÁ„o*/
							if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
								entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "UNI");
							}

							textoModificado = primeiroCaractere + "<UNI>" +
									entreTags + "</UNI>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "UNI", "", false);
							decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
						}
					}

					matcher = padraoEdificacao.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "ED");
						}

						boolean ePlural = (matcher.group(2).endsWith("s") ? true: false);

						textoModificado = primeiroCaractere + "<ED>" + entreTags + "</ED>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "ED", "", ePlural);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoGrupo.matcher(decretoSaida);
					while (matcher.find()){

						entreTags = matcher.group(1);
						if (nomeArquivo.contains("41.476")){
							System.out.println(entreTags);
						}
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);
						entreTags = entreTags.split(" do Quadro")[0];

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "GRUP");
						}

						textoModificado = primeiroCaractere + "<GRUP>"
								+ entreTags + "</GRUP>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "GRUP", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoSecretaria2.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						textoModificado = " <SECR>" + matcher.group(1).trim() + "</SECR>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoSecretaria.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						if (nomeArquivo.contains("41.476")){
							System.out.println(entreTags);
						}
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "SECR");
						}

						textoModificado = primeiroCaractere + "<SECR>" 
								+ entreTags + "</SECR>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, 
								"SECR", "", (matcher.group(6) != null ||  (entreTags.contains(" e ") && entreTags.substring(5).contains("ecretaria")))? true: false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}
					
					matcher = padraoInst2.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						textoModificado = " <INST>" + entreTags.trim() + "</INST>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoInst.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						if (!(entreTags.contains("Procuradoria") && entreTags.contains("Estado"))){
							primeiroCaractere = entreTags.charAt(0);
							entreTags = entreTags.substring(1);

							/*Para identificar se uma sigla faz parte do seleÁ„o*/
							if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
								entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "INST");
							}

							textoModificado = primeiroCaractere + "<INST>";
							textoModificado +=  entreTags + "</INST>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "INST", "", false);
							decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
						}
					}

					matcher = padraoDoc2.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(0);
						textoModificado = " <DOC>" + matcher.group(0).trim() + "</DOC>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoDoc3.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(0);
						primeiroCaractere = entreTags.charAt(0);
						//contÈm o txt sem caractere inicial
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "DOC");
						}

						String tipoDoc = matcher.group(2);
						boolean ePlural = (tipoDoc.endsWith("s") ? true: false);
						tipoDoc = tipoDoc.toLowerCase();

						if (tipoDoc.endsWith("is")) tipoDoc = tipoDoc.substring(0, tipoDoc.length() - 2) + "l";
						else if (tipoDoc.endsWith("ıes")) tipoDoc = tipoDoc.substring(0, tipoDoc.length() - 3) + "„o";
						else if (tipoDoc.endsWith("s")) tipoDoc = tipoDoc.substring(0, tipoDoc.length() - 1);

						tipoDoc = " tipo=\'" + tipoDoc + "\'";
						textoModificado = primeiroCaractere + "<DOC" + tipoDoc + ">" + 
								entreTags + "</DOC>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "DOC", tipoDoc, ePlural);
						textoModificado = adicionarAtributos(textoModificado);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags + "\\E", textoModificado);
					}

					matcher = padraoDoc.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(0);
						if (entreTags.contains("ImÛveis EdÌsio")) continue;
						if(nomeArquivo.contains("41.909"))
							System.out.println(entreTags);
						primeiroCaractere = entreTags.charAt(0);
						//contÈm o txt sem caractere inicial
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "DOC");
						}

						String tipoDoc = matcher.group(2);
						boolean ePlural = (tipoDoc.endsWith("s") ? true: false);
						tipoDoc = tipoDoc.toLowerCase();

						if (tipoDoc.endsWith("is")) tipoDoc = tipoDoc.substring(0, tipoDoc.length() - 2) + "l";
						else if (tipoDoc.endsWith("ıes")) tipoDoc = tipoDoc.substring(0, tipoDoc.length() - 3) + "„o";
						else if (tipoDoc.endsWith("s")) tipoDoc = tipoDoc.substring(0, tipoDoc.length() - 1);

						tipoDoc = " tipo=\'" + tipoDoc + "\'";
						textoModificado = primeiroCaractere + "<DOC" + tipoDoc + ">" + 
								entreTags + "</DOC>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "DOC", tipoDoc, ePlural);
						textoModificado = adicionarAtributos(textoModificado);
						if (textoModificado.split("</(UNI|GRUP|INST|ED|SECR)>").length 
								> textoModificado.split("<(UNI|GRUP|INST|ED|SECR)>").length){
							textoModificado = textoModificado.replace("</UNI></DOC>", "</DOC></UNI>");
							textoModificado = textoModificado.replace("</GRUP></DOC>", "</DOC></GRUP>");
							textoModificado = textoModificado.replace("</INST></DOC>", "</DOC></INST>");
							textoModificado = textoModificado.replace("</ED></DOC>", "</DOC></ED>");
							textoModificado = textoModificado.replace("</SECR></DOC>", "</DOC></SECR>");
						} /*else if (textoModificado.split("</(CAR|UNI|GRUP|INST|ED)>").length > textoModificado.split("<(CAR|UNI|GRUP|INST|ED)>").length){
							textoModificado = textoModificado.replace("</CAR></DOC>", "</DOC></CAR>");
							textoModificado = textoModificado.replace("</UNI></DOC>", "</DOC></UNI>");
							textoModificado = textoModificado.replace("</GRUP></DOC>", "</DOC></GRUP>");
							textoModificado = textoModificado.replace("</INST></DOC>", "</DOC></INST>");
							textoModificado = textoModificado.replace("</ED></DOC>", "</DOC></ED>");
						}*/
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags + "\\E", textoModificado);
					}

					matcher = padraoRegime.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "REG");
						}

						textoModificado = primeiroCaractere + "<REG>" + entreTags + "</REG>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "REG", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			

					}

					matcher = padraoFundo.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "FUN");
						}

						textoModificado = primeiroCaractere + "<FUN>" + entreTags + "</FUN>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "FUN", "", false);

						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoGratificacao.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "GRAT");
						}

						textoModificado = primeiroCaractere + "<GRAT>" + 
								entreTags + "</GRAT>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "GRAT", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			

					}

					matcher = padraoPremio.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "PREM");
						}

						textoModificado = primeiroCaractere + "<PREM>" + 
								entreTags + "</PREM>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "PREM", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoServico2.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						textoModificado = " <SERV>" + matcher.group(1).trim() + "</SERV>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoServico.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "SERV");
						}

						textoModificado = primeiroCaractere + "<SERV>" + 
								entreTags + "</SERV>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "SERV", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoCargo.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(2);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						String aux = (matcher.group(1) != null? matcher.group(1): "2");						

						if (!(aux.contains("nidade")||aux.contains("omiss„o")
								||aux.contains("omitÍ")||aux.contains("lano")
								|| aux.contains("Nota") || aux.contains("OrÁamento") 
								|| aux.contains("venida"))){ //(C|c)omitÍ, (c|C)omiss„o...

							/*Para identificar se uma sigla faz parte do seleÁ„o*/
							if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
								entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "CAR");
							}

							textoModificado = primeiroCaractere + "<CAR>";
							textoModificado += entreTags + "</CAR>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "CAR", "", false);
							decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);
						}
					}

					matcher = padraoCargo2.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(2);
						String aux = (matcher.group(1) != null? matcher.group(1): "2");

						if (!(aux.contains("nidade")||aux.contains("omiss„o")
								||aux.contains("omitÍ")||aux.contains("lano")
								|| aux.contains("ota"))){
							textoModificado = " <CAR>" + matcher.group(2).trim() + "</CAR>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "CAR", "", false);
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
						}
					}

					matcher = padraoEvento.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "EV");
						}

						textoModificado = primeiroCaractere + "<EV>";
						if (!entreTags.equalsIgnoreCase("Congresso Nacional")){
							textoModificado += entreTags + "</EV>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "EV", "", false);
							decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
						}
					}

					matcher = padraoEncargo.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						if (entreTags.split(" - ").length >=2 
								&& !entreTags.contains("CONFINS")){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "ENC");
						}

						textoModificado = primeiroCaractere + "<ENC>" + entreTags + "</ENC>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "ENC", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoIndice.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "IND");
						}

						textoModificado = primeiroCaractere + "<IND>" + 
								entreTags + "</IND>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "IND", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoSistema.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);

						if (entreTags.contains(", Mun") || entreTags.contains(", nesse") 
								|| entreTags.contains(", neste")){
							entreTags = entreTags.split(", Mun")[0];
							entreTags = entreTags.split(", nesse")[0];
							entreTags = entreTags.split(", neste")[0];
						}
						textoModificado = " <SIST>" + entreTags.trim() + "</SIST>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoProgProj.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						if (!entreTags.contains("EstratÈgicos")){
							primeiroCaractere = entreTags.charAt(0);
							entreTags = entreTags.substring(1);

							if (entreTags.split(" - ").length >=2 
									&& !entreTags.contains("Conex„o Cidad„")){							
								entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "PROG");
							}

							textoModificado = primeiroCaractere + "<PROG>" + 
									entreTags + "</PROG>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "PROG", "", false);

							if (textoModificado.split("</CAR>").length != textoModificado.split("<CAR>").length){
								textoModificado = textoModificado.replace("</CAR></PROG>", "</PROG></CAR>");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);
						}
					}

					matcher = padraoPartido.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						primeiroCaractere = entreTags.charAt(0);
						entreTags = entreTags.substring(1);

						/*Para identificar se uma sigla faz parte do seleÁ„o*/
						if (entreTags.split(" -").length >=2 || entreTags.split("\\Q(\\E").length >=2){								
							entreTags = verificarSeSiglaCorrespondeAoNome(entreTags, "PART");
						}

						textoModificado = primeiroCaractere + "<PART>" + 
								entreTags + "</PART>";
						textoModificado = removerdorSinaisFinaisEntreTags(
								textoModificado, "PART", "", false);
						decretoSaida = decretoSaida.replaceFirst("\\Q" + primeiroCaractere + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoData.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						textoModificado = " <DAT>" + matcher.group(1).trim() + "</DAT>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoIntervaloData.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(0);
						textoModificado = " <INT_DAT>" + matcher.group(0).trim() + "</INT_DAT>";						
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoInfoEmp.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(3);
						textoModificado = " <INF_EMP>" + matcher.group(3).trim() + "</INF_EMP>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoEnd.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(1);
						textoModificado = "<END>" + matcher.group(1).trim() + "</END>";
						if (!decretoSaida.contains(textoModificado)){
							decretoSaida = decretoSaida.replaceAll("\\Q" + entreTags +"\\E", textoModificado);
						}
					}

					matcher = padraoIE.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(3);
						textoModificado = " <IE>" + matcher.group(3).trim() + "</IE>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoCNPJ.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(3);
						textoModificado = " <CNPJ>" + matcher.group(3).trim() + "</CNPJ>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoCACEPE.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(0);
						textoModificado = " <CACEPE>" + matcher.group(0).trim() + "</CACEPE>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoMunicipio.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(10);
						if (!entreTags.contains("Pernambuco")){
							textoModificado = " <MUN>" + matcher.group(10).trim() + "</MUN>";
							textoModificado = removerdorSinaisFinaisEntreTags(
									textoModificado, "MUN", "", matcher.group(7) != null ? true : false);
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						}
					}

					matcher = padraoLocal.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(2);
						textoModificado = " <LOC>" + matcher.group(2).trim() + "</LOC>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoSite.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(0);
						textoModificado = " <SITE>" + matcher.group(0).trim() + "</SITE>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					decretoSaida = decretoSaida.replaceAll("&middot;", "\u00B7");
					decretoSaida = decretoSaida.replaceAll("&sup2;","\u00B2");
					decretoSaida = decretoSaida.replaceAll(" m2"," m\u00B2");
					decretoSaida = decretoSaida.replaceAll(" m3"," m\u00B3");
					decretoSaida = decretoSaida.replaceAll("&sup3;","\u00B3");
					decretoSaida+= "</DECR>";

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
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					fw = new FileWriter(new File(arquivoPasta + "\\\\" + nomeArquivo));
					bw = new BufferedWriter(fw);
					bw.write(decretoSaida);
					bw.flush();//antes os dois era bw
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}//fim do for para cada decreto

			try {
				List<String> contador;
				contador = new ArrayList<>(quantidadeDecretosAnexos.keySet());
				for (int v = 0; v < contador.size(); v++){	
					bwb.write(contador.get(v) + "    " + quantidadeDecretosAnexos.get(contador.get(v)));
					bwb.newLine();
					bwc.write(contador.get(v) + "    " + quantidadeAnexosTabela.get(contador.get(v)));					
					bwc.newLine();
					bwf.write(contador.get(v) + "    " + quantidadeTabelaOrcamentarias.get(contador.get(v)));
					bwf.newLine();
					bwg.write(contador.get(v) + "    " + quantidadeDecretosTabela.get(contador.get(v)));
					bwg.newLine();
					bwh.write(contador.get(v) + "    " + quantidadeDecretosTabelaOrcamentaria.get(contador.get(v)));
					bwh.newLine();
					bwi.write(contador.get(v) + "    " + somaDecretosTabela.get(contador.get(v)));
					bwi.newLine();
					bwj.write(contador.get(v) + "    " + somaDecretosTabelaOrcamentaria.get(contador.get(v)));
					bwj.newLine();
				}

				contador = new ArrayList<>(nomePessoas.keySet());
				for (int v =0; v < contador.size(); v++){
					bwd.write(contador.get(v) + "    " + nomePessoas.get(contador.get(v)));
					bwd.newLine();
				}
			}  catch (IOException e) {
				System.out.println("Erro na escrita da tabela ou do anexo");
			}
		} //fim do for para decreto ano

		try {
			bwb.flush();
			bwb.close();
			bwc.flush();
			bwc.close();
			bwd.flush();
			bwd.close();
			bwf.flush();
			bwf.close();
			bwg.flush();
			bwg.close();
			bwh.flush();
			bwh.close();
			bwi.flush();
			bwi.close();
			bwj.flush();
			bwj.close();
		}  catch (IOException e) {
			System.out.println("Os streams foram fechados corretamente.");
		}
	}

	public static String verificarSeSiglaCorrespondeAoNome(String entreTags, String tag){
		if (nomeArquivo.contains("42.247")){
			System.out.println("----------------" + entreTags);
		}
		String[] aux = entreTags.split(" -");
		if (aux.length > 1 && !(tag.equals("DOC") || entreTags.contains("CPRH")) && entreTags.charAt(0) != (entreTags.split(" -")[1]).trim().charAt(0)){
			entreTags = entreTags.split(" -")[0];	
		} 
		aux = entreTags.split("( )?\\Q(\\E");
		if (aux.length > 1 && !(tag.equals("DOC") || entreTags.contains("CPRH")) && entreTags.charAt(0) != (entreTags.split("( )?\\Q(\\E")[1]).trim().charAt(0)){
			entreTags = entreTags.split("( )?\\Q(\\E")[0];
		}

		return entreTags;
	}

	public static boolean verificarSeTabela(String entreTags, String tipo){
		boolean isTabela = false;
		if (entreTags.contains("TBL")){
			isTabela = true;
			quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);
			if (entreTags.contains("R$") || entreTags.contains("TOTAL")){ //È uma tabela orÁament·ria
				quantidadeTabelaOrcamentarias.put(tipo, quantidadeTabelaOrcamentarias.get(tipo) + 1);
			}
		}
		return isTabela;
	}

	public static String tabelaReformulada (String tabela, String tipoTabela){
		Matcher matcher;
		String aux;
		tabela = tabela.replaceAll("<(/)?td>", "");
		tabela = tabela.replaceAll("<(/)?tr>", "");
		tabela = tabela.replaceAll("<(/)?tbody>", "");
		tabela = tabela.replaceAll("\t", "  ");

		matcher = Pattern.compile("\\(([A-Z«¡…Õ”⁄¬ ‘√’ ]+)\\)").matcher(tabela);
		while (matcher.find()){
			aux = matcher.group(1);
			tabela = tabela.replace("(" +matcher.group(1)+ ")", " ");
			tabela = tabela.replaceFirst("<TBL>","\n<TBL\ttipo=\'orcamentaria\'>\n<TIT>" + aux.trim() + "</TIT>\n ");	
		}

		matcher = Pattern.compile(" \\d{5}\\s*- [</>A-Z«¡…Õ”⁄¬ ‘√’¿, -]+ ").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst(matcher.group(0), "\n<DESCR_ENT>\n<ID_ORG_G>" + matcher.group(0).trim() + "</ID_ORG_G> ");
		}


		matcher = Pattern.compile(" \\d{2}\\.\\d{3}\\.\\d{4}\\.\\d{3,5}\\s*(- )?[\\Q</>-,(). \'\\EA-Z«¡…Õ”⁄¬ ‘√’¿‡a-zÁ·ÈÌÛ˙‚ÍÙ„ı]+ ").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst(matcher.group(0), " \n<AT_PROJ>" + matcher.group(0).trim() + "</AT_PROJ> ");
		}

		matcher = Pattern.compile("( Atividade\\s*| \\QOp.\\E\\s*Especial\\s*| OperaÁ„o\\s*Especial\\s*| Projeto\\s*)(:| )\\s*<AT_PROJ>").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst(matcher.group(1) + matcher.group(2), " <DESCR_PROJ><TIPO>" + matcher.group(1).trim() + "</TIPO>" + matcher.group(2));
		}

		matcher = Pattern.compile(" \\d{5}\\s*(- )?[</>A-Z«¡…Õ”⁄¬ ‘√’¿‡a-zÁ·ÈÌÛ˙‚ÍÙ„ı, -]+ ").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst(matcher.group(0), "\n<ID_ORG>" + matcher.group(0).trim() + "</ID_ORG> ");
		}

		matcher = Pattern.compile("(FISCAL)?\\s+\\d{4} ").matcher(tabela);
		while (matcher.find()){
			if (matcher.group(1) == null){
				tabela = tabela.replaceFirst(matcher.group(0), " <FON>" + matcher.group(0).trim() + "</FON> ");
			}
		}

		matcher = Pattern.compile("(TOTAL)?(\\s+(\\d{1,3}\\.)?(\\d{1,3}\\.)?\\d{3},\\d{2})").matcher(tabela);
		while (matcher.find()){
			if (matcher.group(1) != null){
				tabela = tabela.replaceFirst(matcher.group(2), " <TOT>" + matcher.group(2).trim() + "</TOT> ");
			} else {
				tabela = tabela.replaceFirst(matcher.group(2), " <VAL>" + matcher.group(2).trim() + "</VAL> ");
			}
		}

		matcher = Pattern.compile(" \\d\\.\\d\\.\\d{2}\\.\\d{2}\\s+(- )?[</>A-Z«¡…Õ”⁄¬ ‘√’¿‡a-zÁ·ÈÌÛ˙‚ÍÙ„ı, -.]+ ").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst(matcher.group(0), " \n<DESCR_OGM><OGM>" + matcher.group(0).trim() + "</OGM> ");
		}

		matcher = Pattern.compile(" \\d{4}\\.\\d{2}\\.\\d{2}\\s+(- )?[</>A-Z«¡…Õ”⁄¬ ‘√’¿, -.]+ ").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst(matcher.group(0), " \n<DESCR_OGM><OGM>" + matcher.group(0).trim() + "</OGM> ");
		}

		matcher = Pattern.compile("</OGM>(\\s*(<FON>[0-9]{4}</FON>\\s*)?<VAL>[0-9.,]+</VAL>\\s*)").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst(matcher.group(1), " " + matcher.group(1).trim() + "</DESCR_OGM> ");  //+ matcher.group(3) REMOVI P FAZER UM TESTE, MAS ANTES TAVA CERTO
		}

		//CONTINUA«√O(\\s*<DESCR_OGM>|\\s*</TBL>|\\s*</DESCR_PROJ>)
		matcher = Pattern.compile("(<OGM>[A-Z¡…Õ”⁄¬ ‘√’«¿‡Áa-z·ÈÌÛ˙‚ÍıÙ„0-9\\Q<>\'\",()/ -.\\E]+)(</OGM>\\s*<VAL>[0-9.,]+</VAL>\\s*</DESCR_OGM>\\s*)([A-Za-z‡·ÈÌÛ˙‚ÍÙ„ıÁ«¡¿…Õ”⁄¬ ‘√’«\\Q/- .,\\E]{3,})").matcher(tabela);
		while(matcher.find()){
			if (!matcher.group(3).contains("TOTAL")){
				String auxi = matcher.group(1) + " " + matcher.group(3) + matcher.group(2);
				tabela = tabela.replaceFirst(matcher.group(), auxi);
			}
		}

		tabela = tabela.replaceAll("</TBL>[\\s0-9A-Z¡…Õ”⁄¬ ‘√’«$a-z·ÈÌÛ˙‚ÍÙ„ıÁ]*<TBL>", "");
		tabela = tabela.replaceAll("[\\s0-9A-Z¡…Õ”⁄¬ ‘√’«$a-z·ÈÌÛ˙‚ÍÙ„ıÁ]{10,}<DESCR_ENT>", "<DESCR_ENT>");
		//corrige a tabela. textos que est„o fora de at_proj passam a estar nele contidos		

		matcher = Pattern.compile("(<AT_PROJ>[A-Z¡…Õ”⁄¬ ‘√’«¿‡Áa-z·ÈÌÛ˙‚ÍıÙ„0-9\\Q<>\'\",()/ -.\\E]+)(</AT_PROJ>\\s*<VAL>[0-9.,]+</VAL>\\s*)([A-Za-z‡·ÈÌÛ˙‚ÍÙ„ıÁ«¡¿…Õ”⁄¬ ‘√’«\\Q/- .,\\E]{3,})(\\s*<DESCR_OGM>)").matcher(tabela);
		while (matcher.find()){
			String auxi = matcher.group(1) + " " + matcher.group(3) + matcher.group(2) + matcher.group(4);
			tabela = tabela.replaceFirst(matcher.group(), auxi);
		}

		matcher = Pattern.compile("(<AT_PROJ>[\\Q</>-,(). \'\\EA-Z«¡…Õ”⁄¬ ‘√’¿‡a-zÁ·ÈÌÛ˙‚ÍÙ„ı0-9]+</AT_PROJ>\\s*<VAL>[0-9.,]+</VAL>(\\s*<DESCR_OGM>[A-Z¡…Õ”⁄¬ ‘√’«¿‡Áa-z‡·ÈÌÛ˙‚ÍÙ„ı0-9\\Q<>\'\",()/ -.\\E]+</DESCR_OGM>)+\\s*)(TOTAL|<DESCR_ENT>)").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst("\\Q" + matcher.group(1) + "\\E", matcher.group(1).trim() + "</DESCR_PROJ> ");
		}

		matcher = Pattern.compile("(</AT_PROJ>\\s*<VAL>[0-9\\Q,.\\E]+</VAL>\\s*)(</DESCR_OGM>)([\\Q-,(). \'\\EA-Z«¡…Õ”⁄¬ ‘√’¿‡a-zÁ·ÈÌÛ˙‚ÍÙ„ı]+)(<DESCR_OGM>)").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst("\\Q" + matcher.group(0) + "\\E", matcher.group(3) + matcher.group(1) + matcher.group(2) + matcher.group(4));
		}
		
		matcher = Pattern.compile("(</OGM>\\s*<VAL>[0-9\\Q,.\\E]+</VAL>\\s*)(</DESCR_OGM>)([\\Q-,(). \'\\EA-Z«¡…Õ”⁄¬ ‘√’¿‡a-zÁ·ÈÌÛ˙‚ÍÙ„ı]+)(TOTAL|<DESCR_ENT>)").matcher(tabela);
		while (matcher.find()){
			tabela = tabela.replaceFirst("\\Q" + matcher.group(0) + "\\E", matcher.group(3) + matcher.group(1) + matcher.group(2) + matcher.group(4));
		}


		tabela = tabela.replaceAll("\\s{2,}", " ");
		tabela = tabela.replaceAll("<DESCR_PROJ>", "</DESCR_PROJ>\n<DESCR_PROJ>");
		tabela = tabela.replaceAll("<DESCR_ENT>", "</DESCR_ENT>\n<DESCR_ENT>");	

		String novaTabela = "";
		String [] aux2 = tabela.split("<ID_ORG_G>");
		for (int let = 0; let < aux2.length; let++){
			if (let != 0){
				//antes era DESCR_OGM
				aux2[let] = aux2[let].replaceFirst("</DESCR_PROJ>", "");
				aux2[let] = "<ID_ORG_G>" + aux2[let];
			}
			novaTabela += aux2[let];
		}

		tabela = novaTabela;
		tabela = tabela.replaceFirst("</DESCR_ENT>", "");

		if (tabela.contains("DESCR_ENT")){
			matcher = Pattern.compile("(</DESCR_PROJ>\\s*|</DESCR_OGM>\\s*)(TOTAL|</TBL>)").matcher(tabela);
			while (matcher.find()){
				tabela = tabela.replaceAll(matcher.group(0), matcher.group(1).trim() + "</DESCR_ENT> " + matcher.group(2));
			}
		}

		tabela = tabela.replaceFirst("<TOT", "<TOT tipoTabela=\"" + "\"");

		//matcher = Pattern.compile("\\s*[A-Za-z]+\\s*(</DESCR_OGM>)").matcher(tabela);
		//while (matcher.find()){
		//tabela = tabela.replaceFirst(matcher.group(1), "");
		//regrasPassou += "   FIM";
		//}

		//matcher = Pattern.compile("(<[A-Z/_]+>)\\s*(<[A-Z/_]+>)").matcher(tabela);
		//while (matcher.find()){
		//if (matcher.group(1).equals(matcher.group(2))){
		//tabela = tabela.replaceFirst(matcher.group(1), "");
		//}
		return tabela;
	}

	public static String removerdorSinaisFinaisEntreTags(String entreTags, String tag,
			String atributosTag, boolean ehPlural){
		boolean ehPraAddTag = false;
		Matcher m;
		if (entreTags.contains("<</")){
			entreTags = entreTags.replace("<</" + tag, "</" + tag);
			ehPraAddTag = true;
		}

		m = Pattern.compile(" [a-z0-9·ÈÌÛ˙‚ÍÓ]( )?<").matcher(entreTags);
		if (m.find()){
			entreTags = entreTags.split(" [a-z0-9·ÈÌÛ˙‚ÍÓ]( )?<")[0] + "</" + tag + ">"
					+ m.group(0).replace("<", "");
		}

		if (entreTags.contains(", </")){
			entreTags = entreTags.replace(", </" + tag + ">", "</" + tag);
			if (ehPlural){
				entreTags = partirInst‚nciasDaTag(entreTags, tag, atributosTag);
			}
			entreTags = entreTags + ">, ";
		} else if (entreTags.contains(",<") || entreTags.contains(", <")){
			entreTags = entreTags.replace(",</" + tag + ">", "</" + tag);
			if (ehPlural){
				entreTags = partirInst‚nciasDaTag(entreTags, tag, atributosTag);
			}
			entreTags = entreTags + ">,";
		} else {
			if (entreTags.contains(";<")){
				entreTags = entreTags.replace(";</" + tag + ">", "</" + tag + ">;");
			} else if (entreTags.contains(":<")){
				entreTags = entreTags.replace(":</" + tag + ">", "</" + tag + ">:");
			} else if (entreTags.contains(".<") && !tag.equals("EMP")){
				entreTags = entreTags.replace(".</" + tag + ">", "</" + tag + ">.");
			} else if (entreTags.contains(")<")){
				entreTags = entreTags.replace(")</" + tag + ">", "</" + tag + ">)");
			} else if (entreTags.contains("(<")){
				entreTags = entreTags.replace("(</" + tag + ">", "</" + tag + ">(");
			} else {
				Pattern pat = Pattern.compile("( [a-z·ÈÌÛ˙‚ÍÙ‡]( )?)</");
				Matcher mat = pat.matcher(entreTags);
				if (mat.find()){
					String aux = mat.group(1);
					entreTags = entreTags.replaceFirst(" [a-z·ÈÌÛ˙‚ÍÙ‡]( )?</" + tag + ">", "</" + tag + ">" + aux);
				}
			}

			if (ehPlural){
				entreTags = partirInst‚nciasDaTag(entreTags, tag, atributosTag);
			}
		}

		if(ehPraAddTag) entreTags = entreTags + "<";

		return entreTags;
	}

	public static String partirInst‚nciasDaTag(String entreTags, String tag, String atributosTag){
		entreTags = entreTags.replaceAll(", ", "</" + tag + ">, <" + tag + atributosTag + ">");
		if (tag.equals("MUN") || tag.equals("DOC") || tag.equals("SECR")){
				entreTags = entreTags.replaceFirst(" e ", "</" + tag + "> e <" + tag + ">");
			entreTags = entreTags.replace("Abreu</MUN> e <MUN>", "Abreu e ").replace("</DOC> e <DOC>FunÁıes", " e FunÁıes")
					.replace("Planejamento</SECR> e <SECR>Gest„o", "Planejamento e Gest„o");
		}
		
		entreTags = entreTags.replaceFirst("e <" + tag + ">", "e <" + tag + atributosTag + ">");
		return entreTags;
	}


	public static String adicionarAtributos(String entreTags){
		Pattern p = Pattern.compile("([0-9]{2,6})(/|-)?([0-9]{2,4})?");
		Matcher m = p.matcher(entreTags);
		Matcher mat = Pattern.compile("([a-z])\'>").matcher(entreTags);
		String aux;
		while(m.find()){
			if (m.group(3) != null && mat.find()){
				aux = mat.group(1);
				entreTags = entreTags.replaceFirst(aux + "\'>", aux + "\' numeracao=\'" + m.group(1) + "\' ano=\'" + m.group(3) + "\'>");
			} else if (m.group(3) == null && mat.find()){
				aux = mat.group(1);
				entreTags = entreTags.replaceFirst(aux + "\'>", aux + "\' numeracao=\'" + m.group(1) + "\'>");
			}
		}
		return entreTags;
	}

	public static String classificadorAnexos(String entreTags, String tipo){
		String textoModificado;
		String semAnexo = entreTags.
				replaceFirst("\\s*</td>(\\s*<td>\\s*</td>\\s*)+(</tr>)?\\s*","");

		//QQ coisa remover os valores em romano
		semAnexo = entreTags.
				replaceFirst("ANEXO\\s*(⁄NICO|I{1,3}|IV|VI{0,3})\\s*</td>\\s*</tr>","");

		String unicoOuNumero = "";
		/*
				entreTags.replaceFirst("ANEXO\\s+(⁄NICO|\\d{1,2}|[IVX]{1,4})\\s+(</td>\\s*</tr>)?", "");
		//coloca o n˙mero do anexo
		String unicoOuNumero = entreTags.replaceFirst("\\Q" + semAnexo + "\\E", "");
		if (unicoOuNumero.contains("</td> </tr>")){
			unicoOuNumero = unicoOuNumero.replaceFirst("</td>\\s*</tr>", "");
			semAnexo = "<TBL>" + semAnexo;
		}
		*/
		if (semAnexo.contains("</TBL>") && !semAnexo.contains("<TBL")){
			semAnexo = "<TBL>" + semAnexo;
		}

		if (semAnexo.substring((int) (semAnexo.length()/2)).contains("DESCRI«√O")){
			textoModificado = unicoOuNumero + " <DESCR>" + semAnexo.trim() + "</DESCR>";
		} else if (semAnexo.substring((int) (semAnexo.length()/2)).contains("MEMORIAL")){
			textoModificado = unicoOuNumero + " <MEMO>" + semAnexo.trim() + "</MEMO>";
		} else if (semAnexo.substring((int) (semAnexo.length()/2)).contains("PLANO")){
			textoModificado = unicoOuNumero + " <PLAN>" + semAnexo.trim() + "</PLAN>";
		} else if (semAnexo.substring((int) (semAnexo.length()/2)).contains("FORMUL¡RIO")){
			textoModificado = unicoOuNumero + " <FORM>" + semAnexo.trim() + "</FORM>";
		} else if (semAnexo.substring((int) (semAnexo.length()/2)).contains("C”DIGO")){
			textoModificado = unicoOuNumero + " <COD>" + semAnexo.trim() + "</COD>";
		} else if (semAnexo.substring((int) (semAnexo.length()/2)).contains("RELAT”RIO")){
			textoModificado = unicoOuNumero + " <REL>" + semAnexo.trim() + "</REL>";
		} else if (semAnexo.substring((int) (semAnexo.length()/2)).contains("REGIMENTO")){
			textoModificado = unicoOuNumero + " <REGTO>" + semAnexo.trim() + "</REGTO>";
		} else if (semAnexo.substring((int) (semAnexo.length()/2)).contains("REGULAMENTO")){
			textoModificado = unicoOuNumero + " <REGUL>" + semAnexo.trim() + "</REGUL>";
		} else if (!entreTags.contains("TBL")){
			textoModificado = unicoOuNumero + " <OUTRO>" + semAnexo.trim() + "</OUTRO>";
		} else {
			textoModificado = unicoOuNumero + " <TAB>" + semAnexo.trim() + "</TAB>";	
			quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);
			if (entreTags.contains("R$") || entreTags.contains("TOTAL")){ //È uma tabela orÁament·ria
				quantidadeTabelaOrcamentarias.put(tipo, quantidadeTabelaOrcamentarias.get(tipo) + 1);
			}
		}
		
		//Se n„o possuir TAB, pode-se remover o TBL
		if(!textoModificado.contains("TAB")){
			textoModificado = textoModificado.replace("<TBL>", "");
			textoModificado = textoModificado.replace("</TBL>", "");
		}
		
		return textoModificado; 
	}

	public static void popularAsHashtables(){
		nomePessoas.put("teste", 0);

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
		contadorTiposDecreto.put("Dispıe", 0);
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
		quantidadeDecretosAnexos.put("Dispıe", 0);
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
		quantidadeAnexosTabela.put("Dispıe", 0);
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

		quantidadeTabelaOrcamentarias.put("Abre", 0);
		quantidadeTabelaOrcamentarias.put("Acrescenta", 0);
		quantidadeTabelaOrcamentarias.put("Aloca", 0);
		quantidadeTabelaOrcamentarias.put("Altera", 0);
		quantidadeTabelaOrcamentarias.put("Amplia", 0);
		quantidadeTabelaOrcamentarias.put("Aprova", 0);
		quantidadeTabelaOrcamentarias.put("Autoriza", 0);
		quantidadeTabelaOrcamentarias.put("Atualiza", 0);
		quantidadeTabelaOrcamentarias.put("Ativa", 0);
		quantidadeTabelaOrcamentarias.put("Concede", 0);
		quantidadeTabelaOrcamentarias.put("Convoca", 0);
		quantidadeTabelaOrcamentarias.put("Cria", 0);
		quantidadeTabelaOrcamentarias.put("Declara", 0);
		quantidadeTabelaOrcamentarias.put("Decreta", 0);
		quantidadeTabelaOrcamentarias.put("Define", 0);
		quantidadeTabelaOrcamentarias.put("Delega", 0);
		quantidadeTabelaOrcamentarias.put("Desativa", 0);
		quantidadeTabelaOrcamentarias.put("Disciplina", 0);
		quantidadeTabelaOrcamentarias.put("Dispıe", 0);
		quantidadeTabelaOrcamentarias.put("Eleva", 0);
		quantidadeTabelaOrcamentarias.put("Estabelece", 0);
		quantidadeTabelaOrcamentarias.put("Estende", 0);
		quantidadeTabelaOrcamentarias.put("Homologa", 0);
		quantidadeTabelaOrcamentarias.put("Incorpora", 0);
		quantidadeTabelaOrcamentarias.put("Institui", 0);
		quantidadeTabelaOrcamentarias.put("Interpreta", 0);
		quantidadeTabelaOrcamentarias.put("Introduz", 0);
		quantidadeTabelaOrcamentarias.put("Modifica", 0);
		quantidadeTabelaOrcamentarias.put("Promove", 0);
		quantidadeTabelaOrcamentarias.put("Prorroga", 0);
		quantidadeTabelaOrcamentarias.put("Qualifica", 0);
		quantidadeTabelaOrcamentarias.put("Reabre", 0);
		quantidadeTabelaOrcamentarias.put("Redenomina", 0);
		quantidadeTabelaOrcamentarias.put("Regulamenta", 0);
		quantidadeTabelaOrcamentarias.put("Relaciona", 0);
		quantidadeTabelaOrcamentarias.put("Renova", 0);
		quantidadeTabelaOrcamentarias.put("Revoga", 0);
		quantidadeTabelaOrcamentarias.put("Transfere", 0);
		quantidadeTabelaOrcamentarias.put("Transforma", 0);

		quantidadeDecretosTabela.put("Abre", 0);
		quantidadeDecretosTabela.put("Acrescenta", 0);
		quantidadeDecretosTabela.put("Aloca", 0);
		quantidadeDecretosTabela.put("Altera", 0);
		quantidadeDecretosTabela.put("Amplia", 0);
		quantidadeDecretosTabela.put("Aprova", 0);
		quantidadeDecretosTabela.put("Autoriza", 0);
		quantidadeDecretosTabela.put("Atualiza", 0);
		quantidadeDecretosTabela.put("Ativa", 0);
		quantidadeDecretosTabela.put("Concede", 0);
		quantidadeDecretosTabela.put("Convoca", 0);
		quantidadeDecretosTabela.put("Cria", 0);
		quantidadeDecretosTabela.put("Declara", 0);
		quantidadeDecretosTabela.put("Decreta", 0);
		quantidadeDecretosTabela.put("Define", 0);
		quantidadeDecretosTabela.put("Delega", 0);
		quantidadeDecretosTabela.put("Desativa", 0);
		quantidadeDecretosTabela.put("Disciplina", 0);
		quantidadeDecretosTabela.put("Dispıe", 0);
		quantidadeDecretosTabela.put("Eleva", 0);
		quantidadeDecretosTabela.put("Estabelece", 0);
		quantidadeDecretosTabela.put("Estende", 0);
		quantidadeDecretosTabela.put("Homologa", 0);
		quantidadeDecretosTabela.put("Incorpora", 0);
		quantidadeDecretosTabela.put("Institui", 0);
		quantidadeDecretosTabela.put("Interpreta", 0);
		quantidadeDecretosTabela.put("Introduz", 0);
		quantidadeDecretosTabela.put("Modifica", 0);
		quantidadeDecretosTabela.put("Promove", 0);
		quantidadeDecretosTabela.put("Prorroga", 0);
		quantidadeDecretosTabela.put("Qualifica", 0);
		quantidadeDecretosTabela.put("Reabre", 0);
		quantidadeDecretosTabela.put("Redenomina", 0);
		quantidadeDecretosTabela.put("Regulamenta", 0);
		quantidadeDecretosTabela.put("Relaciona", 0);
		quantidadeDecretosTabela.put("Renova", 0);
		quantidadeDecretosTabela.put("Revoga", 0);
		quantidadeDecretosTabela.put("Transfere", 0);
		quantidadeDecretosTabela.put("Transforma", 0);

		quantidadeDecretosTabelaOrcamentaria.put("Abre", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Acrescenta", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Aloca", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Altera", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Amplia", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Aprova", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Autoriza", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Atualiza", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Ativa", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Concede", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Convoca", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Cria", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Declara", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Decreta", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Define", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Delega", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Desativa", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Disciplina", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Dispıe", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Eleva", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Estabelece", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Estende", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Homologa", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Incorpora", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Institui", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Interpreta", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Introduz", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Modifica", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Promove", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Prorroga", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Qualifica", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Reabre", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Redenomina", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Regulamenta", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Relaciona", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Renova", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Revoga", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Transfere", 0);
		quantidadeDecretosTabelaOrcamentaria.put("Transforma", 0);
	}

	public static void popularAsHashtablesSoma(){
		somaDecretosTabelaOrcamentaria.put("Abre", 0);
		somaDecretosTabelaOrcamentaria.put("Acrescenta", 0);
		somaDecretosTabelaOrcamentaria.put("Aloca", 0);
		somaDecretosTabelaOrcamentaria.put("Altera", 0);
		somaDecretosTabelaOrcamentaria.put("Amplia", 0);
		somaDecretosTabelaOrcamentaria.put("Aprova", 0);
		somaDecretosTabelaOrcamentaria.put("Autoriza", 0);
		somaDecretosTabelaOrcamentaria.put("Atualiza", 0);
		somaDecretosTabelaOrcamentaria.put("Ativa", 0);
		somaDecretosTabelaOrcamentaria.put("Concede", 0);
		somaDecretosTabelaOrcamentaria.put("Convoca", 0);
		somaDecretosTabelaOrcamentaria.put("Cria", 0);
		somaDecretosTabelaOrcamentaria.put("Declara", 0);
		somaDecretosTabelaOrcamentaria.put("Decreta", 0);
		somaDecretosTabelaOrcamentaria.put("Define", 0);
		somaDecretosTabelaOrcamentaria.put("Delega", 0);
		somaDecretosTabelaOrcamentaria.put("Desativa", 0);
		somaDecretosTabelaOrcamentaria.put("Disciplina", 0);
		somaDecretosTabelaOrcamentaria.put("Dispıe", 0);
		somaDecretosTabelaOrcamentaria.put("Eleva", 0);
		somaDecretosTabelaOrcamentaria.put("Estabelece", 0);
		somaDecretosTabelaOrcamentaria.put("Estende", 0);
		somaDecretosTabelaOrcamentaria.put("Homologa", 0);
		somaDecretosTabelaOrcamentaria.put("Incorpora", 0);
		somaDecretosTabelaOrcamentaria.put("Institui", 0);
		somaDecretosTabelaOrcamentaria.put("Interpreta", 0);
		somaDecretosTabelaOrcamentaria.put("Introduz", 0);
		somaDecretosTabelaOrcamentaria.put("Modifica", 0);
		somaDecretosTabelaOrcamentaria.put("Promove", 0);
		somaDecretosTabelaOrcamentaria.put("Prorroga", 0);
		somaDecretosTabelaOrcamentaria.put("Qualifica", 0);
		somaDecretosTabelaOrcamentaria.put("Reabre", 0);
		somaDecretosTabelaOrcamentaria.put("Redenomina", 0);
		somaDecretosTabelaOrcamentaria.put("Regulamenta", 0);
		somaDecretosTabelaOrcamentaria.put("Relaciona", 0);
		somaDecretosTabelaOrcamentaria.put("Renova", 0);
		somaDecretosTabelaOrcamentaria.put("Revoga", 0);
		somaDecretosTabelaOrcamentaria.put("Transfere", 0);
		somaDecretosTabelaOrcamentaria.put("Transforma", 0);


		somaDecretosTabela.put("Abre", 0);
		somaDecretosTabela.put("Acrescenta", 0);
		somaDecretosTabela.put("Aloca", 0);
		somaDecretosTabela.put("Altera", 0);
		somaDecretosTabela.put("Amplia", 0);
		somaDecretosTabela.put("Aprova", 0);
		somaDecretosTabela.put("Autoriza", 0);
		somaDecretosTabela.put("Atualiza", 0);
		somaDecretosTabela.put("Ativa", 0);
		somaDecretosTabela.put("Concede", 0);
		somaDecretosTabela.put("Convoca", 0);
		somaDecretosTabela.put("Cria", 0);
		somaDecretosTabela.put("Declara", 0);
		somaDecretosTabela.put("Decreta", 0);
		somaDecretosTabela.put("Define", 0);
		somaDecretosTabela.put("Delega", 0);
		somaDecretosTabela.put("Desativa", 0);
		somaDecretosTabela.put("Disciplina", 0);
		somaDecretosTabela.put("Dispıe", 0);
		somaDecretosTabela.put("Eleva", 0);
		somaDecretosTabela.put("Estabelece", 0);
		somaDecretosTabela.put("Estende", 0);
		somaDecretosTabela.put("Homologa", 0);
		somaDecretosTabela.put("Incorpora", 0);
		somaDecretosTabela.put("Institui", 0);
		somaDecretosTabela.put("Interpreta", 0);
		somaDecretosTabela.put("Introduz", 0);
		somaDecretosTabela.put("Modifica", 0);
		somaDecretosTabela.put("Promove", 0);
		somaDecretosTabela.put("Prorroga", 0);
		somaDecretosTabela.put("Qualifica", 0);
		somaDecretosTabela.put("Reabre", 0);
		somaDecretosTabela.put("Redenomina", 0);
		somaDecretosTabela.put("Regulamenta", 0);
		somaDecretosTabela.put("Relaciona", 0);
		somaDecretosTabela.put("Renova", 0);
		somaDecretosTabela.put("Revoga", 0);
		somaDecretosTabela.put("Transfere", 0);
		somaDecretosTabela.put("Transforma", 0);

	}
}
