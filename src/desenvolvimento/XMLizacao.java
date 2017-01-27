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
		FileWriter fwb;
		BufferedWriter bwb = null;
		FileWriter fwc;
		BufferedWriter bwc = null;
		FileWriter fwd;
		BufferedWriter bwd = null;
		FileWriter fwf;
		BufferedWriter bwf = null;
		FileWriter fwarq;
		BufferedWriter bwarq = null;

		String nomeArquivo;
		File arquivoPasta;
		Map<String, Integer> contadorTiposDecreto = new Hashtable<>();

		//Regex
		//\u00B2\u00B3\u00B7
		String regexIdentificacao = "(DECRETO N∫)\\s+((\\d{2}.\\d{3})(,|.)? (DE|DIA) (\\d{1,2})(∞|∫)? DE ([A-Z«]+) DE (\\d{4}).)";
		String regexTipoDecreto = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Dispıe|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco)";
		String regexTipoDecreto2 = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Dispıe|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"
				+ "([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERCÕCIO DO CARGO DE GOVERNADOR DO ESTADO)";
		String regexAtribuicoes = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(CONSIDERANDO|DECRETA|DECRETO)";
		String regexAtribuicoes2 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAtribuicoes3 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Art)";
		String regexConsideracoes = "(CONSIDERANDO)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,ß∫∞™./<>-_\\\\E \n\t\u00A7\u002D]+)(DECRETA)";
		String regexCorpo = "(DECRETA|DECRETO[^ N∞]{3})([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal·cio do Campo das Princesas)";
		String regexCorpo4 = "(DECRETA|DECRETO[^ N∞]{3})([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal·cio do Campo das Princesas[A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄« ,∫0-9\\.]+(ANEXO))";
		String regexCorpo2 = "(DECRETA|DECRETO[^ N∞]{3})([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexCorpo3 = "(Art)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal·cio do Campo das Princesas)";
		String regexFinalmentes = "(O )?(Pal·cio do Campo das Princesas)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(IndependÍncia do Brasil(\\.)?)";
		String regexAssinaturas = "([A-Z¬√¡¿… Õ”‘’⁄« \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exercÌcio)?([A-Z¬√¡¿… Õ”‘’⁄« \']+)";
		String regexAssinaturas2 = "([A-Z¬√¡¿… Õ”‘’⁄« \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exercÌcio)?([A-Z¬√¡¿… Õ”‘’⁄« \']+)(ANEXO)";
		String regexAnexos = "(NO )?(ANEXO)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;=/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexAnexos2 = "(NO )?(ANEXO)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;=/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)(ERRATA [^PUBLI]+)";
		String regexErrata = "(ERRATA)([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;=/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)";
		String regexInfo = "(\\Q(ERRATA PUBLICADA NO DI¡RIO OFICIAL DE \\E\\d{1,2} DE [A-Z«]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SAÕDO COM INCORRE«√O NO ORIGINAL)\\E|\\Q(Revogado\\E [0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q!?&$%:@#;,/ß∫∞™.<>-_\\\\E \n\t\u00A7\u002D]{5,70}\\)|\\Q(Vide errata no final do texto.)\\E)";

		//REMOVER O TANTO DE CARACTERES QUE TEM EM ASSINATURA
		String regexConsiderando = "<CONSIDERACOES>([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#;=/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)</CONSIDERACOES>";
		String regexAssinatura = "<ASS>([A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\'\\Q()=,.<>-\\\\E \n\t]+)</ASS>";
		//antes junto com a vÌrgula, tinha espaÁo para escolher
		// devido a isso, existia logo apÛs a opÁ„o "de" sem espaÁo
		//primeiro coment·rio deixa de ser escolha
		String regexLeis = "(( Decreto| \\QDecreto-Lei\\E| Decreto Lei| Decreto Lei Federal| Decreto-Lei Federal| Lei Federal| Lei Complementar| Lei Complementar Federal| Lei| ResoluÁ„o| Emenda Constitucional| Ad Referendum| OfÌcio| Parecer| Parecer Conjunto| Portaria Conjunta| Portaria| Ato( DeclaratÛrio)?| Termo| Ordem Banc·ria| Ajuste| Ordens Banc·rias| Di·rio Oficial (do Estado|da Uni„o)| Ata)([A-Z \\Q/-\\E]{0,16})((( n∫| n∞)? (\\d{1,2}\\.\\d{3}|\\d{3}|\\d{2}|\\d{2,4}/\\d{2,4}[A-Z\\Q/ -\\E]{0,10}))?(,)?((( de| em)? (\\d{1,2})(∞|∫)?( de |[\\Q./-\\E])([a-zÁA-Z«]{4,9}|\\d{1,2}[\\Q./-\\E]))?( de )?(\\d{4}))?)( da ConstituiÁ„o Estadual)?)(,|\\Q.\\E|;|\\Q:\\E| [a-z]|\\))";
		String regexDinheiro = " R\\$\\s+[0-9.]+,\\d{2}";
		String regexPorcentagem = " [0-9]+(,\\d{1,})?( )?%";
		//[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*
		String regexEmpresa = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|antiga|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)?( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{1,3})+(\\QLTDA.\\E|LTDA)( ME)?(\\s*(-| |/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*))(,|\\Q.\\E|;|:| [a-z]|, [a-z])";
		String regexEmpresa2 = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|antiga|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)?( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{1,3})+(\\QS/A\\E|\\QS.A.\\E|\\QS.A\\E)(\\s*(-| |/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*))(,|\\Q.\\E|;|:| [a-z])";
		String regexEmpresa3 = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{1,3})+(\\s*(-| |/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*))(,|\\Q.\\E|;|:)";
		String regexEmpresa4 = "( (a|‡|pela|da|da mesma|denominaÁ„o atual È|atualmente denominada|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)( (([0-9A-Z¬¡… Õ”‘⁄&]|de |da |do |das |dos )[0-9A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]*[\\Q-., \\E]{0,3})+))(,|\\Q.\\E|;|:| [a-z])";
		String regexOrgao = "(( FundaÁ(„o|ıes)| EscritÛrio(s)?| ”rg„o(s)?| OrganizaÁ(„o|ıes)| Secretaria(s)?| Instituto(s)?| Departamento(s)?| Procuradoria(s)?| AgÍncia(s)?| Comiss(„o|ıes)?| Junta(s)?| Assembleia(s)?| C‚mara(s)?| ConsÛrcio(s)?| Defensoria(s)?| Tribuna(l|is)| Companhia(s)?| Controladoria(s)?| Gabinete(s)?| GerÍncia(s)?| ComitÍ(s)?| Contadoria(s)?| InteligÍncia(s)?| Unidade(s)?| Grupo(s)?| Grupamento(s)?| Corpo(s)?| Centra(l|is)| Conselho(s)?| Banco(s)?| PolÌcia(s)?| Diretoria(s)?| Receita| Delegacia(s)?| Autarquia(s)?| CoordenaÁ(„o|ıes)| Assessoria(s)?) ((e )?(de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)[\\Q-., \\E]{0,3})+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)(,|,<|\\Q.\\E|;|:| [a-z])";
		String regexProgProj = "(( Regime(s)?| Simples| Projeto(s)?| Programa(s)?| Fundo(s)?| PolÌtica(s)?| GratificaÁ(„o|ıes)| ConsolidaÁ(„o|ıes)| Sistema(s)?| Plano(s)?| Regulamento(s)?| ConvÍnio(s)?| Financiamento(s)?| Quadro(s)?| OrÁamento(s)?| DeclaraÁ(„o|ıes)| Certificado(s)?) ((e )?(de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?(<ORG>|<CAR>)?[A-Z¬¡¿… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)[\\Q-., \\E]{0,3}(</ORG>|</CAR>)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)(,|\\Q.\\E|;|:|<|[a-z]| [a-z])";
		String regexEvento = "(( [IVX]{1,5})?( ConferÍncia| Evento| Show| Concerto| SimpÛsio| Debate| FÛrum| Estudo)( (e )?(de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(,|:)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\QÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«&\\E]+)*)(,|\\Q.\\E|;|:|<| [a-z])";
		String regexData = " (\\d{1,2})(∞|∫)? de ([a-zÁA-Z«]{4,9}) (de )?(\\d{4})| \\d{1,2}[\\Q./\\E]\\d{1,2}[\\Q./\\E]\\d{2,4}";
		String regexIntervaloData = " <DAT>[0-9A-Za-z \\Q∫∞/.\\E]+</DAT> a <DAT>[0-9A-Za-z \\Q∫∞/.\\E]+</DAT>| \\d{1,2}(∞|∫)?([ A-Za-z]{4,10})? (a|e) <DAT>[0-9A-Za-z \\Q∫∞./\\E]+</DAT>";
		String regexInfoEmp = " estabelecida n(a|o) ([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\\Q() ,∫∞™-.;/'\"\\E]+ CNPJ(/MF)? n(∫|∞) \\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2} e CACEPE n(∞|∫) \\d{7}\\-\\d{2})";
		String regexCNPJ = " CNPJ(/MF)?( n∞| n∫)?( \\d{2}\\.\\d{3}\\.\\d{3}(/\\d{4}-\\d{2})?)";
		String regexIE = " (InscriÁ„o Estadual|IE)( n∞| n∫)?( \\d{1,3}\\.\\d{3}\\.\\d{3}(\\-\\d{1,2}|\\.\\d{3})?)";
		String regexCACEPE = " \\d{7}\\-\\d{2}";
		String regexMunicipio = "( (M|m)unicÌpio)( de)?( [A-Z¬¡¿… Õ”‘⁄][A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«]+)";
		String regexCargo = "(( Primeiro| Segundo| Terceiro| (1|2|3)(∫|∞|™))?( Comandante( Geral)?| Chefe(s)?| Sargent(o|a)| Tenente(s)?| Subtenente(s)?| (\\QTenente\\E( |-))?Corone(l|Èis)| Cabo(s)?| Capit„(o)?(s)?| Major| Secret·ri(o|a)| PresidÍncia| Diretor(\\-Presidente|\\-Geral)?| Agente(s)?| Gerente(s)?| Assistente(s)?| Assessor(a|es|as)?)( (e )?(de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?(<ORG>)?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(</ORG>)?[\\Q-., \\E]{0,3})+)(,|\\Q.\\E|;|:| [a-z])";
		String regexCargo2 = "( Delegad(o|a)(s)?| Escriv(„o|„es)| Agente(s)?| Diretor(es|a|as)?| Assessor(a|as|es)?)(,| [a-z ]{1,8}[^A-Z]|\\.)";
		String regexImp = "(( Õndice| Taxa| Imposto| Tributo)( ((e )?(de |do |dos |da |das |‡ |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?[A-Z¬¡… Õ”‘⁄]([A-Z¬√¡… Õ”‘’⁄«]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙]+)(,|:)?))+(\\s*(-|/)\\s*[A-Z]([A-Z¬√¡… Õ”‘’⁄«&]+|[a-zÁ„‡·‚ÈÍÌÛÙı˙&]+))*)(,|\\Q.\\E|;|:)";
		String regexSite = " www(\\d)?((\\.[a-z]+))+((/)?|/( )?[A-Za-z]+)";
		String regexEnd = "<INF_EMP>([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\\Q() ,∫∞™-.;/'\"\\E]+), com CNPJ(/MF)?";
		String regexAnexo = "<ANEXOS>([0-9A-Za-zÁ„‡·‚ÈÍÌÛÙı˙¬√¡¿… Õ”‘’⁄«\"\'\\Q()!?&$ß%:@#=;/,∫∞™.<>-_\\\\E \n\t\u00A7\u002D]+)</ANEXOS>";//sÛ esse tem sinal igual

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
		Pattern padraoDinheiro = Pattern.compile(regexDinheiro);
		Pattern padraoPorcentagem = Pattern.compile(regexPorcentagem);
		Pattern padraoEmpresa = Pattern.compile(regexEmpresa);
		Pattern padraoEmpresa2 = Pattern.compile(regexEmpresa2);
		Pattern padraoEmpresa3 = Pattern.compile(regexEmpresa3);
		Pattern padraoEmpresa4 = Pattern.compile(regexEmpresa4);
		Pattern padraoProgProj = Pattern.compile(regexProgProj);
		Pattern padraoEvento = Pattern.compile(regexEvento);
		Pattern padraoData = Pattern.compile(regexData);
		Pattern padraoIntervaloData = Pattern.compile(regexIntervaloData);
		Pattern padraoInfoEmp = Pattern.compile(regexInfoEmp);
		Pattern padraoCNPJ = Pattern.compile(regexCNPJ);
		Pattern padraoIE = Pattern.compile(regexIE);
		Pattern padraoCACEPE = Pattern.compile(regexCACEPE);
		Pattern padraoMunicipio = Pattern.compile(regexMunicipio);
		Pattern padraoCargo = Pattern.compile(regexCargo);
		Pattern padraoCargo2 = Pattern.compile(regexCargo2);
		Pattern padraoImp = Pattern.compile(regexImp);
		Pattern padraoEnd = Pattern.compile(regexEnd);
		Pattern padraoSite = Pattern.compile(regexSite);
		Pattern padraoOrgao = Pattern.compile(regexOrgao);
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
		tiposDocumentos.put("Di·rio Oficial da Uni„o", "DOU");
		tiposDocumentos.put("Di·rio Oficial do Estado", "DOE");
		//n„o t· pegando
		tiposDocumentos.put("Ordem Banc·ria", "OB");
		tiposDocumentos.put("Ordens Banc·rias", "OB");

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

		Hashtable<String, Integer> quantidadeDecretosAnexos = new Hashtable<>();
		Hashtable<String, Integer> quantidadeAnexosTabela = new Hashtable<>();
		Hashtable<String, Integer> quantidadeTabelaOrcamentarias= new Hashtable<>();
		Hashtable<String, Integer> nomePessoas = new Hashtable<>();

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


		String tipo = "";
		String nomePessoa;
		String entreTags;
		String textoModificado;
		String[] segmentosConsAss;
		//AQUI COME«A A REMO«√O DA FORMATA«√O
		File b = new File("anexos");
		File c = new File("tabelas");
		File d = new File("nomePessoas");
		File f = new File("tabelaOrcamentaria");

		try {
			b.createNewFile();
			c.createNewFile();
			d.createNewFile();
			f.createNewFile();

			fwb = new FileWriter(b);
			bwb = new BufferedWriter(fwb);

			fwc = new FileWriter(c);
			bwc = new BufferedWriter(fwc);

			fwd = new FileWriter(d);
			bwd = new BufferedWriter(fwd);

			fwf = new FileWriter(f);
			bwf = new BufferedWriter(fwf);
		} catch (IOException e1) {
			System.out.println("Os arquivos est„o defeituosos.");
		}

		for (int i = 0; i < anosDecretos.length; i++){
			File a = new File("decretos" + anosDecretos[i]);


			arquivoPasta = new File(caminhoDecretos + anosDecretos[i] + "\\" + pastaDestino);
			System.out.println(arquivoPasta);
			if (!arquivoPasta.exists()){ //Se pasta n„o existe, cria
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
					decretoSaida = "";



					matcher = padraoInfo.matcher(decretoIntermediario);

					decretoSaida+= "<DECRETO>\n";
					while (matcher.find()){
						decretoSaida += "<INFO>" + matcher.group(0) + "</INFO>";
						decretoIntermediario = decretoIntermediario.replaceFirst("\\Q"+ matcher.group(0)+ "\\E", "");
					}

					matcher = padraoIdentificacao.matcher(decretoIntermediario);
					matcherAuxiliar = null;

					if (matcher.find()){
						decretoSaida += "<ID numeracao=" + matcher.group(3) + " data=" + (matcher.group(6).length() < 2? "0" + matcher.group(6) : matcher.group(6)) + "-" + numeracaoMeses.get(matcher.group(8).toLowerCase())
						+ "-" + matcher.group(9) + ">" + matcher.group(1).trim() + " " + matcher.group(2) + "</ID>\n<CORPO>";
						decretoIntermediario = decretoIntermediario.replace(matcher.group(0), "");
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "IDENTIFICACAO");
						bwarq.newLine();
					}

					matcher = padraoTipoDecreto.matcher(decretoIntermediario);
					matcherAuxiliar = padraoTipoDecreto2.matcher(decretoIntermediario);

					if (matcherAuxiliar.find()){
						entreTags = matcherAuxiliar.group(1) + " " + matcherAuxiliar.group(2);
						decretoSaida += "<DESCR>" +	entreTags + "<";
						tipo = matcherAuxiliar.group(1);
						contadorTiposDecreto.put(matcherAuxiliar.group(1), contadorTiposDecreto.get(matcherAuxiliar.group(1)) + 1);
						decretoIntermediario = decretoIntermediario.replace(entreTags, "");
					} else if (matcher.find()){
						entreTags = matcher.group(1) + " " + matcher.group(2);
						decretoSaida += "<DESCR>" + entreTags + "<";
						tipo = matcher.group(1);
						contadorTiposDecreto.put(matcher.group(1), contadorTiposDecreto.get(matcher.group(1)) + 1);
						decretoIntermediario = decretoIntermediario.replace(entreTags, "");
					} else{
						bwarq.write(i + "     " + nomeArquivo + "     " + "DESCRICAO");
						bwarq.newLine();
					}

					decretoSaida = decretoSaida.replaceFirst("O\\s*<", " ");
					String[] partes = decretoSaida.split("GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO");
					decretoSaida = partes[0].trim();
					decretoSaida += "</DESCR>";

					matcher = padraoAtribuicoes.matcher(decretoIntermediario);
					matcherAuxiliar = padraoAtribuicoes2.matcher(decretoIntermediario);

					decretoSaida += "<ATRIB>";
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
					decretoSaida += "</ATRIB>";

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
						decretoSaida += "<DECRETAMENTO>" + matcher.group(1).trim() + " "
								+ matcher.group(2).trim() + "</DECRETAMENTO>";
					} else if (matcherAuxiliar.find()){
						decretoSaida += "<DECRETAMENTO>" + matcherAuxiliar.group(1).trim() + " "
								+ matcherAuxiliar.group(2).trim() + "</DECRETAMENTO>";
					} else{
						matcher = padraoCorpo2.matcher(decretoIntermediario);
						matcherAuxiliar = padraoCorpo3.matcher(decretoIntermediario);
						if (matcher.find()) {
							decretoSaida += "<DECRETAMENTO>" + matcher.group(1).trim() + " "
									+ matcher.group(2).trim() + "</DECRETAMENTO>";
						} else if (matcherAuxiliar.find()){
							decretoSaida += "<DECRETAMENTO>" + matcherAuxiliar.group(1).trim() + " "
									+ matcherAuxiliar.group(2).trim() + "</DECRETAMENTO>";	
						}else{
							bwarq.write(i + "     " + nomeArquivo + "     " + "DECRETAMENTO");
							bwarq.newLine();
						}
					}

					decretoSaida+= "</CORPO>";
					matcher = padraoFinalmentes.matcher(decretoIntermediario);
					matcherAuxiliar = null;

					if (matcher.find()){
						decretoSaida += "<FINALMENTES>\n";
						if (matcher.group(1) != null) decretoSaida +=  matcher.group(1).trim();
						decretoSaida += matcher.group(2).trim() + " " + matcher.group(3).trim() + " " +
								matcher.group(4).trim() +  "\n</FINALMENTES>";
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
						decretoSaida += "  " + matcherAuxiliar.group(4).replaceFirst("ERRATA[ A-Z¬√¡¿… Õ”‘’⁄«]+","").trim() + "</ASS>";
					} else if (matcher.find()){
						decretoSaida += "<ASS>" + matcher.group(1).trim() + " "
								+ matcher.group(2);
						if (matcher.group(3) != null) decretoSaida += " " + matcher.group(3);
						decretoSaida +=  "  " + matcher.group(4).replaceFirst("ERRATA[ A-Z¬√¡¿… Õ”‘’⁄«]+","").trim() + "</ASS>";
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
						decretoSaida += "<ERR>" + matcher.group(1).trim() + " "
								+ matcher.group(2).replaceFirst("(\\Q(ERRATA PUBLICADA NO DI¡RIO OFICIAL DE \\E\\d{1,2} DE [A-Z«]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SAÕDO COM INCORRE«√O NO ORIGINAL)\\E)" , "").trim()
								+ "</ERR>";
					} 

					matcher = padraoConsiderando.matcher(decretoSaida);
					if (matcher.find()){
						entreTags = matcher.group(1);
						segmentosConsAss = entreTags.split("( )+CONSIDERANDO");
						textoModificado = "";
						for (int k = 0; k < segmentosConsAss.length; k++){
							if (k != 0)
								textoModificado += "<CONS>" + ("CONSIDERANDO" + segmentosConsAss[k]).trim() + "</CONS>";
							else
								textoModificado += "<CONS>" + segmentosConsAss[k].trim() + "</CONS>";
						}
						decretoSaida = decretoSaida.replaceFirst(entreTags, textoModificado);
					}

					matcher = padraoAssinatura.matcher(decretoSaida);
					if (matcher.find()){
						entreTags = matcher.group(1);
						segmentosConsAss = entreTags.split("( ){2,}");
						textoModificado = "";
						nomePessoa = segmentosConsAss[0].replaceFirst("Governador do Estado|GOVERNADOR DO ESTADO", "").trim();
						textoModificado += "<ASS_GOV>" + nomePessoa + "</ASS_GOV>" + segmentosConsAss[0].replaceFirst(nomePessoa, "").trim();

						if (nomePessoas.containsKey(nomePessoa)){
							nomePessoas.replace(nomePessoa, nomePessoas.get(nomePessoa) + 1);
						} else {
							nomePessoas.put(nomePessoa, 1);
						}
						for (int k = 1; k < segmentosConsAss.length; k++){
							//System.out.println(segmentosConsAss[k]);
							nomePessoa = segmentosConsAss[k].trim();
							textoModificado += "<ASS_OUTRO>" + nomePessoa + "</ASS_OUTRO>";
							if (nomePessoas.containsKey(nomePessoa)){
								nomePessoas.replace(nomePessoa, nomePessoas.get(nomePessoa) + 1);
							} else {
								nomePessoas.put(nomePessoa, 1);
							}
						}
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoLeis.matcher(decretoSaida);
					//TALVEZ MUDAR AQUI
					while (matcher.find() && (matcher.group(7) != null || matcher.group(11) != null || matcher.group(12) != null)){
						entreTags = matcher.group(1);
						textoModificado = " <" +  tiposDocumentos.get(matcher.group(2).trim());
						System.out.println(entreTags);

						if (matcher.group(11) == null && matcher.group(7) != null){
							if (matcher.group(9).contains("/")){
								textoModificado += " numeracao=" + matcher.group(9).split("/")[0] + " ano=" + matcher.group(9).split("/")[1];
							} else{
								textoModificado += " numeracao=" + matcher.group(9);
							}

						} else if(matcher.group(7) == null && matcher.group(14) != null){
							textoModificado += " data=" + (matcher.group(14).length() < 2? "0" + matcher.group(14) : matcher.group(14))
									+ "-";
							if  (matcher.group(16).contains(".") || matcher.group(16).contains("/") || matcher.group(16).contains("-")){
								textoModificado += (matcher.group(17).length() < 2? "0" + matcher.group(17): matcher.group(17)) + "-" + matcher.group(19);
							} else{
								textoModificado += numeracaoMeses.get(matcher.group(17).toLowerCase()) + "-" + matcher.group(19);
							}
						} else if (matcher.group(13) != null){
							textoModificado += " numeracao=" + matcher.group(9);
							textoModificado += " data=" + (matcher.group(14).length() < 2? "0" + matcher.group(14) : matcher.group(14))
									+ "-";
							if  (matcher.group(16).contains(".") || matcher.group(16).contains("/") || matcher.group(16).contains("-")){
								textoModificado += (matcher.group(17).length() < 2? "0" + matcher.group(17): matcher.group(17)) + "-" + matcher.group(19);
							} else{
								textoModificado += numeracaoMeses.get(matcher.group(17).toLowerCase()) + "-" + matcher.group(19);
							}
							//+ numeracaoMeses.get(matcher.group(16).toLowerCase()) + "-" + matcher.group(17);
						} else if (matcher.group(11) != null){
							textoModificado += " numeracao=" + matcher.group(9);
							textoModificado += " ano=" + matcher.group(19);
						}

						String tipoDoc = tiposDocumentos.get(matcher.group(2).trim());
						textoModificado+= ">" + matcher.group(1).trim() + "</" + tipoDoc + ">";
						if (textoModificado.contains(",<")){
							textoModificado.replace(",</" + tipoDoc +  ">", "</" + tipoDoc +  ">,");
						} else if (textoModificado.contains(";<")){
							textoModificado.replace(";</" + tipoDoc +  ">", "</" + tipoDoc +  ">;");
						} else if (textoModificado.contains(":<")){
							textoModificado.replace(":</" + tipoDoc +  ">", "</" + tipoDoc +  ">:");
						}
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

					matcher = padraoEmpresa.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						if (matcher.group(3) == null){
							entreTags = matcher.group(3);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						}		
					}

					matcher = padraoEmpresa2.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);

						if (matcher.group(3) == null){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						}	
					}

					matcher = padraoEmpresa3.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						if (matcher.group(3) == null){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						}				
					}

					matcher = padraoEmpresa4.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						if (matcher.group(3) == null){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("Empresa")||matcher.group(3).contains("EMPRESA")){
							entreTags = matcher.group(3) + matcher.group(4);
							textoModificado = " <EMP>" + (matcher.group(3) + matcher.group(4)).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						} else if (matcher.group(3).contains("empresa") && matcher.group(4).charAt(0) == matcher.group(4).toUpperCase().charAt(0)){
							entreTags = matcher.group(4);
							textoModificado = " <EMP>" + matcher.group(4).trim() + "</EMP>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</EMP>", "</EMP>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</EMP>", "</EMP>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</EMP>", "</EMP>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
						}			
					}

					matcher = padraoOrgao.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						if (!(matcher.group(0).contains("Unidade da FederaÁ„o") || matcher.group(0).contains("Unidade Federativa"))){
							entreTags = matcher.group(1);
							textoModificado = " <ORG>" + matcher.group(1).trim() + "</ORG>";
							if (textoModificado.contains(",<")){
								textoModificado = textoModificado.replace(",</ORG>", "</ORG>,");
							} else if (textoModificado.contains(";<")){
								textoModificado = textoModificado.replace(";</ORG>", "</ORG>;");
							} else if (textoModificado.contains(":<")){
								textoModificado = textoModificado.replace(":</ORG>", "</ORG>:");
							}
							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
						}
					}
					
					matcher = padraoCargo.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						textoModificado = " <CAR>" + matcher.group(1).trim() + "</CAR>";
						if (textoModificado.contains(",<")){
							textoModificado = textoModificado.replace(",</CAR>", "</CAR>,");
						} else if (textoModificado.contains(";<")){
							textoModificado = textoModificado.replace(";</CAR>", "</CAR>;");
						} else if (textoModificado.contains(":<")){
							textoModificado = textoModificado.replace(":</CAR>", "</CAR>:");
						}
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoCargo2.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						textoModificado = " <CAR>" + matcher.group(1).trim() + "</CAR>";
						if (textoModificado.contains(",<")){
							textoModificado = textoModificado.replace(",</CAR>", "</CAR>,");
						} else if (textoModificado.contains(";<")){
							textoModificado = textoModificado.replace(";</CAR>", "</CAR>;");
						} else if (textoModificado.contains(":<")){
							textoModificado = textoModificado.replace(":</CAR>", "</CAR>:");
						}
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}
					
					matcher = padraoEvento.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						textoModificado = " <EV>" + matcher.group(1).trim() + "</EV>";
						if (textoModificado.contains(",<")){
							textoModificado = textoModificado.replace(",</EV>", "</EV>,");
						} else if (textoModificado.contains(";<")){
							textoModificado = textoModificado.replace(";</EV>", "</EV>;");
						} else if (textoModificado.contains(":<")){
							textoModificado = textoModificado.replace(":</EV>", "</EV>:");
						}
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoImp.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						textoModificado = " <TAX>" + matcher.group(1).trim() + "</TAX>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoProgProj.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(1);
						textoModificado = " <PROG>" + matcher.group(1).trim() + "</PROG>";
						if (textoModificado.contains(",<")){
							textoModificado = textoModificado.replace(",</PROG>", "</PROG>,");
						} else if (textoModificado.contains(";<")){
							textoModificado = textoModificado.replace(";</PROG>", "</PROG>;");
						} else if (textoModificado.contains(":<")){
							textoModificado = textoModificado.replace(":</PROG>", "</PROG>:");
						}
						if (textoModificado.split("</CAR>").length != textoModificado.split("<CAR>").length){
							textoModificado = textoModificado.replace("</CAR></PROG>", "</PROG></CAR>");
						}
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			
					}

					matcher = padraoData.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(0);
						textoModificado = " <DAT>" + matcher.group(0).trim() + "</DAT>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoIntervaloData.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(nomeArquivo);
						entreTags = matcher.group(0);
						textoModificado = " <INT_DAT>" + matcher.group(0).trim() + "</INT_DAT>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoInfoEmp.matcher(decretoSaida);
					while (matcher.find()){
						entreTags = matcher.group(2);
						textoModificado = " <INF_EMP>" + matcher.group(2).trim() + "</INF_EMP>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoEnd.matcher(decretoSaida);
					while (matcher.find()){
						//System.out.println(matcher.group(1));
						entreTags = matcher.group(1);
						textoModificado = "<END>" + matcher.group(1).trim() + "</END>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
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
						entreTags = matcher.group(4);
						textoModificado = " <MUN>" + matcher.group(4).trim() + "</MUN>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}

					matcher = padraoSite.matcher(decretoSaida);
					while (matcher.find()){
						System.out.println(matcher.group(1));
						entreTags = matcher.group(0);
						textoModificado = " <SITE>" + matcher.group(0).trim() + "</SITE>";
						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);
					}


					matcher = padraoAnexo.matcher(decretoSaida);
					textoModificado = "";

					if (matcher.find()){
						//System.out.println(nomeArquivo);
						quantidadeDecretosAnexos.containsKey(tipo);
						quantidadeDecretosAnexos.put(tipo, quantidadeDecretosAnexos.get(tipo) + 1);
						entreTags = matcher.group(1);
						if (entreTags.startsWith("ANEXO ⁄NICO")){
							//System.out.println(entreTags);
							if (entreTags.contains("MEMORIAL DESCRITIVO")){
								textoModificado = "<MEMO>" + entreTags + "</MEMO>";
								matcherAuxiliar = padraoDinheiro.matcher(entreTags);
								if (entreTags.contains("table")){
									quantidadeAnexosTabela.containsKey(tipo);
									quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);

									if (entreTags.contains("R$") || matcher.find() || entreTags.contains("crÈdito") || entreTags.contains("CR…DITO")){
										quantidadeTabelaOrcamentarias.containsKey(tipo);
										quantidadeTabelaOrcamentarias.put(tipo, quantidadeTabelaOrcamentarias.get(tipo) + 1);
									}
								}
							} else if (entreTags.contains("PLANO")){
								textoModificado += "<«I>" + "ANEXO " + entreTags.trim() + "</«I>";
							}else if (entreTags.contains("FORMUL¡RIO")){
								textoModificado += "<«E>" + "ANEXO " + entreTags.trim() + "</«E>";
							}
							else if (entreTags.contains("table")){
								textoModificado = "<TAB>" + entreTags + "</TAB>";					
								quantidadeAnexosTabela.containsKey(tipo);
								quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);

								if (entreTags.contains("R$") || matcher.find() || entreTags.contains("crÈdito") || entreTags.contains("CR…DITO")){
									quantidadeTabelaOrcamentarias.containsKey(tipo);
									quantidadeTabelaOrcamentarias.put(tipo, quantidadeTabelaOrcamentarias.get(tipo) + 1);
								}
							} else {
								textoModificado = "<OUTRO>" + entreTags + "</OUTRO>";
								if (entreTags.contains("table")){
									quantidadeAnexosTabela.containsKey(tipo);
									quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);

									if (entreTags.contains("R$") || matcher.find() || entreTags.contains("crÈdito") || entreTags.contains("CR…DITO")){
										quantidadeTabelaOrcamentarias.containsKey(tipo);
										quantidadeTabelaOrcamentarias.put(tipo, quantidadeTabelaOrcamentarias.get(tipo) + 1);
									}
								}
							}
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
							decretoSaida = decretoSaida.replace("<ANEXOS>", "<ANEXOS num_anexos=1>");
						} else {
							segmentosConsAss = entreTags.split("ANEXO [IVX]{1,4}");

							for (int k = 1; k < segmentosConsAss.length; k++){
								if (segmentosConsAss[k].contains("MEMORIAL DESCRITIVO")){
									textoModificado += "<MEMO>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</MEMO>";
								}else if (segmentosConsAss[k].contains("PLANO")){
									textoModificado += "<«I>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</«I>";
								}else if (segmentosConsAss[k].contains("FORMUL¡RIO")){
									textoModificado += "<«E>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</«E>";
								}else if (segmentosConsAss[k].contains("table")){
									textoModificado += "<TAB>" + "ANEXO " + k + segmentosConsAss[k].trim() + "</TAB>";
								} else{
									//System.out.println(nomeArquivo);
								}
							}
							if (entreTags.contains("table")){
								quantidadeAnexosTabela.containsKey(tipo);
								quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);

								if (entreTags.contains("R$") || matcher.find() || entreTags.contains("crÈdito") || entreTags.contains("CR…DITO") || entreTags.contains("ORCAMENT¡RIO")){
									quantidadeTabelaOrcamentarias.containsKey(tipo);
									quantidadeTabelaOrcamentarias.put(tipo, quantidadeTabelaOrcamentarias.get(tipo) + 1);
								}
							}
							decretoSaida = decretoSaida.replace(entreTags, textoModificado);
							//decretoSaida = decretoSaida.replace("<ANEXOS>", "<ANEXOS num_anexos=" + );
						}
					}

					decretoSaida = decretoSaida.replaceAll("&middot;", "\u00B7");
					decretoSaida = decretoSaida.replaceAll("&sup2;","\u00B2");
					decretoSaida = decretoSaida.replaceAll(" m 2;"," m\u00B2");
					decretoSaida = decretoSaida.replaceAll("&sup3;","\u00B3");
					decretoSaida+= "</DECRETO>";

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
					bw.flush();//antes os dois era bw
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		List<String> contador = new ArrayList<>(quantidadeDecretosAnexos.keySet());
		for (int v = 0; v < contador.size(); v++){
			try {
				bwb.write(contador.get(v) + "    " + quantidadeDecretosAnexos.get(contador.get(v)));
				bwb.newLine();
			} catch (IOException e) {
				System.out.println("Erro na escrita tabela ou anexo");
			}
		}
		contador = new ArrayList<>(quantidadeAnexosTabela.keySet());
		for (int v = 0; v < contador.size(); v++){
			try {
				bwc.write(contador.get(v) + "    " + quantidadeAnexosTabela.get(contador.get(v)));
				bwc.newLine();
			} catch (IOException e) {
				System.out.println("Erro na escrita tabela ou anexo");
			}
		}

		contador = new ArrayList<>(quantidadeTabelaOrcamentarias.keySet());
		for (int v = 0; v < contador.size(); v++){
			try {
				bwf.write(contador.get(v) + "    " + quantidadeTabelaOrcamentarias.get(contador.get(v)));
				bwf.newLine();
			} catch (IOException e) {
				System.out.println("Erro na escrita tabela ou anexo");
			}
		}

		contador = new ArrayList<>(nomePessoas.keySet());
		for (int v = 0; v < contador.size(); v++){
			try {
				bwd.write(contador.get(v) + "    " + nomePessoas.get(contador.get(v)));
				bwd.newLine();
			} catch (IOException e) {
				System.out.println("Erro na escrita nome de pessoas");
			}
		}

		try {
			bwb.flush();
			bwb.close();
			bwc.flush();
			bwc.close();
			bwd.flush();
			bwd.close();
			bwarq.flush();
			bwarq.close();
		} catch (IOException e) {
			System.out.println("Erro no fechamento do streaming.");
		}


	}

}
