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
	private static Hashtable<String, Integer> nomePessoas = new Hashtable<>();

	public static void main(String[] args) {
		//Pastas de origem e destino
		String pastaDestino = "XMLizada";
		String pastaOrigem = "semFormata��o";
		//Caminhos dos decretos

		//Substitua para a localiza��o em seu computador

		String caminhoDecretos = "C:\\Users\\JN\\Documents\\DecretosAlepe\\";

		int[] anosDecretos = {2014, 2015, 2016};



		File b = new File("anexosAno");

		File c = new File ("tabelasAno");

		File d = new File("nomePessoas");

		File f = new File("tabelaOrcamentariaAno");



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

		String regexIdentificacao = "(DECRETO N�)\\s+((\\d{2}.\\d{3})(,|.)? (DE|DIA) (\\d{1,2})(�|�)? DE ([A-Z�]+) DE (\\d{4}).)";

		String regexTipoDecreto = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Disp�e|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"

				+ "([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(GOVERNADOR D(O|E) ESTADO|GOVERNO DO ESTADO|Governador do Estado de Pernambuco)";

		String regexTipoDecreto2 = "(Abre|Acrescenta|Aloca|Altera|Amplia|Aprova|Autoriza|Ativa|Atualiza|Concede|Convoca|Cria|Declara|Decreta|Define|Delega|Desativa|Disciplina|Disp�e|Eleva|Estabelece|Estende|Homologa|Incorpora|Institui|Interpreta|Introduz|Modifica|Promove|Prorroga|Qualifica|Reabre|Redenomina|Regulamenta|Renova|Relaciona|Revoga|Transfere|Transforma)"

				+ "([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(O )?(PRESIDENTE DA ASSEMBLEIA LEGISLATIVA DO ESTADO DE PERNAMBUCO, NO EXERC�CIO DO CARGO DE GOVERNADOR DO ESTADO)";

		String regexAtribuicoes = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(CONSIDERANDO|DECRETA|DECRETO)";

		String regexAtribuicoes2 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)";

		String regexAtribuicoes3 = "(O )?(GOVERNO DO ESTADO|GOVERNADOR DE ESTADO|GOVERNADOR DO ESTADO|Governador do Estado de Pernambuco|PRESIDENTE DA ASSEMBLEIA LEGISLATIVA( DO ESTADO)?( DE PERNAMBUCO))([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(Art)";

		String regexConsideracoes = "(CONSIDERANDO)([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,����./<>-_\\\\E \n\t\u00A7\u002D]+)(DECRETA)";

		String regexCorpo = "(DECRETA|DECRETO[^ N�]{3})([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal�cio( do)? Campo das Princesas)";

		String regexCorpo4 = "(DECRETA|DECRETO[^ N�]{3})([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal�cio( do)? Campo das Princesas[A-Za-z������������������������ ,�0-9\\.]+(ANEXO))";

		String regexCorpo2 = "(DECRETA|DECRETO[^ N�]{3})([0-9A-Za-z������������������������\"\'\\Q()!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]+)";

		String regexCorpo3 = "(Art)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;,���.<>-_\\\\E \n\t\u00A7\u002D]+)(Pal�cio( do)? Campo das Princesas)";

		String regexFinalmentes = "(O )?(Pal�cio( do)? Campo das Princesas)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;,���.<>-_\\\\E \n\t\u00A7\u002D]+)(Independ�ncia do Brasil(\\.)?)";

		String regexAssinaturas = "([A-Z������������ \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exerc�cio)?([A-Z������������ \']+)";

		String regexAssinaturas2 = "([A-Z������������ \']+)(Governador do Estado|GOVERNADOR DO ESTADO)( em exerc�cio)?([A-Z������������ \']+)(ANEXO)";

		String regexAnexos = "(NO )?(ANEXO)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)";

		String regexAnexos2 = "(NO )?(ANEXO)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)(ERRATA [^PUBLI]+)";

		String regexErrata = "(ERRATA)([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7\u002D]+)";

		String regexInfo = "(\\Q(ERRATA PUBLICADA NO DI�RIO OFICIAL DE \\E\\d{1,2} DE [A-Z�]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SA�DO COM INCORRE��O NO ORIGINAL)\\E|\\Q(Revogado\\E [0-9A-Za-z������������������������\"\'\\Q!?&$%:@#;,/����.<>-_\\\\E \n\t\u00A7\u002D]{5,70}\\)|\\Q(Vide errata no final do texto.)\\E)";



		//REMOVER O TANTO DE CARACTERES QUE TEM EM ASSINATURA

		String regexConsiderando = "<CONSIDERACOES>([0-9A-Za-z������������������������\"\'\\Q()!?&$�%:@#;=/,���.<>-_\\\\E \n\t\u00A7]+)</CONSIDERACOES>";

		String regexAssinatura = "<ASS>([A-Za-z������������������������\'\\Q()=,.<>-\\\\E \n\t]+)</ASS>";

		String regexLeis = "(( |\"|\\(|>)(Decreto|\\QDecreto-Lei\\E|Decreto Lei|Decreto Lei Federal|\\QDecreto-Lei\\E Federal|Lei Federal|Lei Complementar|Lei Complementar Federal|Lei|Resolu��o|Emenda Constitucional|Ad Referendum|Of�cio|Parecer|Parecer Conjunto|Portaria Conjunta|Portaria|Ato|Ato Declarat�rio|Termo|Ajuste|Ata|Ac�rd�o|Protocolo|Lei Or�ament�ria Anual)( do| da)?[A-Z \\Q/-.\\E]{0,16}(( n�| n�)? (\\d{1,2}\\.\\d{3}|\\d{3}|\\d{2}|\\d{2,4}/\\d{2,4}[A-Z\\Q/ -\\E]{0,10}))?(,)?((( de| em)? (\\d{1,2})(�|�)?( de |[\\Q./-\\E])([a-z�A-Z�]{4,9}|\\d{1,2}[\\Q./-\\E]))?( de )?(\\d{4}))?)(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexDoc = "(( |\"|\\(|>)(Manua(l|is)|Caderno(s)?|C�digo(s)?|Constitui�(�o|�es)|Estatuto(s)?|Termo(s)?|Documento(s)?|Cadastro(s)?|Quadro(s)?|Declara�(�o|�es)|Certificado(s)?|Regimento(s)?|Regulamento(s)?|Ato(s)?|Ata(s)?|Termo(s)?|Orde(m|ns) Banc�ria(s)?|Registro(s)?|Contrato(s)?|Balan�o(s)?|Balancete(s)?|Nota(s)?|Relat�rio(s)?|Resolu�(�o|�es)|Anexo(s)?|Memoria(l|is)|Classifica�(�o|�es)|Conv�nio(s)?) (e )?((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |para |com |com os |com as )?([A-Z��������]|[0-9])([A-Z�����������]+|[a-z������������]+|[0-9]+)*(, | - | (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&-\\E]+)*( [0-9\\Q-/\\E]+)?)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E| [a-z���������]|  )";

		String regexDoc2 = " Or�amento Fiscal| Or�amento de Investimento( das Empresas)?| Di�rio Oficial do Estado| Di�rio Oficial da Uni�o"; 

		String regexDinheiro = " R\\$\\s+[0-9.]+,\\d{2}";

		String regexPorcentagem = " [0-9]+(,\\d{1,})?( )?%";

		String regexEmpresaLTDA = "( (a|�|pela|da|da mesma|denomina��o atual �|atualmente denominada|antiga|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)?( (([0-9A-Z��������&]|de |da |do |das |dos )[0-9A-Za-z\\Q������������������������&\\E]*[\\Q-., \\E]{1,3})+(\\QLTDA.\\E|LTDA)( ME)?(\\s*(-| |/)\\s*[A-Z]([A-Z�����������&]+|[a-z������������&]+))*))(,|\\Q.\\E|;|:|\"| [a-z���������]|, [a-z]|\\))";

		String regexEmpresaSA = "( (a|�|pela|da|da mesma|denomina��o atual �|atualmente denominada|antiga|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)?( (([0-9A-Z��������&]|de |da |do |das |dos )[0-9A-Za-z\\Q������������������������&\\E]*[\\Q-., \\E]{1,3})+(\\QS/A\\E|\\QS.A.\\E|\\QS.A\\E)(\\s*(-| |/)\\s*[A-Z]([A-Z�����������&]+|[a-z������������&]+))*))(,|\\Q.\\E|;|:|\"| [a-z���������]|\\))";

		String regexEmpresaNaoSALTDA = "( (a|�|pela|da|da mesma|denomina��o atual �|atualmente denominada|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)( (([0-9A-Z��������&]|de |da |do |das |dos )[0-9A-Za-z\\Q������������������������&\\E]*[\\Q-., \\E]{1,3})+(\\s*(-| |/)\\s*[A-Z]([A-Z�����������&]+|[a-z������������&]+))*))(,|\\Q.\\E|;|:|\"|\\))";

		String regexEmpresaNaoSALTDASemSilga = "( (a|�|pela|da|da mesma|denomina��o atual �|atualmente denominada|[IXV]{1,5} -)( empresa| EMPRESA| Empresa)( (([0-9A-Z��������&]|de |da |do |das |dos )[0-9A-Za-z\\Q������������������������&\\E]*[\\Q-., \\E]{0,3})+))(,|\\Q.\\E|;|:|\"| [a-z])";

		String regexInst = "(( |\"|\\(|>)(Funda�(�o|�es)|Escrit�rio(s)?|�rg�o(s)?|Organiza�(�o|�es)|Instituto(s)?|Departamento(s)?|Procuradoria(s)?|Ag�ncia(s)?|Junta(s)?|Assembleia(s)?|C�mara(s)?|Cons�rcio(s)?|Defensoria(s)?|Tribuna(l|is)|Companhia(s)?|Conservat�rio(s)?|Controladoria(s)?|Ger�ncia(s)?|Contadoria(s)?|Intelig�ncia(s)?|Corpo(s)?|Centra(l|is)|Banco(s)?|Pol�cia(s)?|Diretoria(s)?|Receita|Delegacia(s)?|Autarquia(s)?|Superintend�ncia(s)?|Minist�rio(s)?|Centro(s)?) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?(<GRUP>)?[A-Z��������]([A-Z�����������]+|[a-z������������]+)(, |-| (e )?)?(</GRUP>)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexInst2 = "( AD Diper| Adagro| Arquivo P�blico Digital| Porto Digital| Porto de Recife| ProRural| Complexo Industrial Portu�rio| Universidade de Pernambuco| Pernambuco Participa��es e Investimento S/A( - Perpart)?| Servi�o de Prote��o ao Consumidor( - PROCON)?| Administra��o Geral de Fernando de Noronha| Congresso Nacional| Laborat�rio Farmac�utico de Pernambuco(\\s*(-)?\\s*LAFEPE)?)";

		String regexGrupo = "(( |\"|\\(|>)(Comiss(�o|�es)|Comit�(s)?|Grupo(s)?|Grupamento(s)?|Conselho(s)?|Coordena�(�o|�es)|Coordenadoria(s)?|Assessoria(s)?|Sociedade(s)?) ((e )?(de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z��������]([A-Z�����������]+|[a-z������������]+)(, | - | )?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexUnidade = "(( |\"|\\(|>)((?i)Unidade) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z��������]([A-Z�����������]+|[a-z������������]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])"; 

		String regexSecretaria = "(( |\"|\\(|>)((?i)(Secretaria)|(?i)(Gabinete)) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexSecretaria2 = "( Assessoria Especial| Casa Militar| Vice-Governadoria| Procuradoria Geral do Estado| Lideran�a do Governo na Assembleia Legislativa)";

		String regexServico2 = "( Delegacia Virtual| Certid�o Negativa| Bolsa de Empregos)";

		String regexServico = "(( |\"|\\(|>)Servi�o ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, | - | (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexProgProj = "(( |\"|\\(|>)(Projeto(s)?|Consolida�(�o|�es)|Programa(s)?|Pol�tica(s)?|Plano(s)?|Pacto(s)?) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?(<CAR>|<ORG>)?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, |-| (e )?)?(</CAR>|</ORG>)?)+(\\s*(-|/| )\\s*([A-Z]|[0-9])([A-Za-z\\Q������������������������&\\E]+|[0-9]+))*| Simples Nacional)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexRegime = "(( |\"|\\(|>)(Regime(s)?) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, |-| (e )?)?(</CAR>|</ORG>)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexFundo = "(( |\"|\\(|>)(Fundo(s)?) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, |-| (e )?)?(</CAR>|</ORG>)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexGratificacao = "(( |\"|\\(|>)(Gratifica�(�o|�es)|Compensa�(�o|�es)|Bonifica�(�o|�es)|Abono(s)?|Benef�cio(s)?) (e )?((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, | (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexPremio = "(( |\"|\\(|>)(Pr�mio(s)?|Medalha(s)?|Men�(�o|�es)|Homenage(m|ns)|Honraria(s)?|Condecora�(�o|�es)|L�urea(s)?) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, |-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexEdificacao = "(( |\"|\\(|>)(Pr�dio(s)?|Edif�cio(s)?|Escola(s)?|Pal�cio(s)?|Pres�dio(s)?|Arena(s)?|Refinaria(s)?|Estaleiro(s)?|Polo(s)?|Esta�(�o|�es)|Coletor(es|a|as)?|Reservat�rio(s)?|Cart�rio(s)?|Hospita(l|is)( Regina(l|is)| Federa(l|is)| Estadua(l|is)| Municipa(l|is))?) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(-| (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z�����������])";

		String regexSistema = "(( |\"|\\(|>)(Sistema|sistema) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?(\\Qe-\\E)?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, | - | (e )?)?)+(\\s*(-|/|\\()\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexEvento = "(( |\"|\\(|>)([IVX]{1,5} |\\d{1,3}(�|�|�) )?(Confer�ncia|Congresso|Evento|Show|Concerto|Simp�sio|Debate|F�rum|Estudo|Copa) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para |com |com os |com as )?[A-Z��������]([A-Z�����������]+|[a-z������������]+)(\\. |, | - | (e )?)?)+((de )?\\d{4})?(\\s*(-|/|\\()\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexPartido = "(( |\"|\\(|>)((?i)Partido(s)?) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |para |com |com os |com as )?[A-Z���������]([A-Z�����������]+|[a-z������������]+)(, | - | (e )?)?)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexData = "( (\\d{1,2})(�|�)? de ([a-z�A-Z�]{4,9}) (do ano )?(de )?(\\d{4})| \\d{1,2}[\\Q./\\E]\\d{1,2}[\\Q./\\E](\\d{2}|\\d{4}))( |\\. |,|;|:|( )?<|\"|\\))";

		String regexIntervaloData = " <DAT>[0-9A-Za-z \\Q��/.\\E]+</DAT> a <DAT>[0-9A-Za-z \\Q��/.\\E]+</DAT>| \\d{1,2}(�|�)?( de)? ([ A-Za-z]{4,10})? (a|e) <DAT>[0-9A-Za-z \\Q��./\\E]+</DAT>| [A-Z�a-z�]{4,9} a [A-Z�a-z�]{4,9} de \\d{4}| \\d{4} a \\d{4}";

		String regexInfoEmp = " (estabelecid|situad|localizad)(a na|o no)( [0-9A-Za-z������������������������\\Q() ,���-.;/'\"\\E]+ CNPJ(/MF)? n(�|�) \\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2} e CACEPE n(�|�) \\d{7}\\-\\d{2})";

		String regexCNPJ = " CNPJ(/MF)?( n�| n�)?( \\d{2}\\.\\d{3}\\.\\d{3}(/\\d{4}-\\d{2})?)";

		String regexIE = " (Inscri��o Estadual|IE)( n�| n�)?( \\d{1,3}\\.\\d{3}\\.\\d{3}(\\-\\d{1,2}|\\.\\d{3})?)";

		String regexCACEPE = " \\d{7}\\-\\d{2}";

		String regexMunicipio = "( (M|m)unic�pio(s)?)( de| do| da)?((( e)?( de| do| dos| da| das)? [A-Z���������][A-Za-z������������������������]+(,|\\.)?)+)";

		String regexCargo = "([A-Za-z������������������������]+)?(( | \"| \\(| >)(ex|EX|Ex)?(\\Q-\\E)?(Primeiro |Segundo |Terceiro |(1|2|3)(�|�|�) )?(Auditor(es)?|Comandante(s)?( Geral)?|Chefe(s)?|Sargent(o|a)(s)?|Tenente(s)?(-)?|Subtenente(s)?|Corone(l|�is)|Capit�(o)?(s)?|Major(es)?|Fiscal|Educador|Cirurgi�o|Secret�ri(o|a)(s)?|Diretor(es|a|as)?(-)?|Agente(s)?|Ministr(o|a)(s)?|Gerente(s)?|Assistente(s)?|Assessor(a|es|as)?|Superintendente(s)?|Coordenador(a|as|es)?|Gestor(es|a|as)?|T�cnico(s)?|M�dic(o|a)(s)?|Terapeuta(s)?|Farmac�utic(o|a)(s)?|Condutor(es)?) (\\s*(destinad(o|a)(s)? |sobre |relativ(o|a)(s)? |com |para )?\\s*(de |do |dos |da |das |� |�s |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na )?(<ORG>)?\\s*[A-Z��������]([A-Z�����������]+|[a-z������������]+)(</ORG>)?(, |\\s*-\\s*|\\s+(e )?)?)+)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexCargo2 = "([A-Za-z������������������������]+)?(( | \"| >| /)(ex|EX|Ex)?(\\Q-\\E)?(Fisioterapeuta|Arte(\\s+|-)Educador(a|as|es)?|Delegad(o|a)(s)?|Escriv(�o|�es)|Agente(s)?|Governador(a|as|es)?( do Estado)?|Psicopedagogo|Arquiteto|Aeroportu�rio|M�dico|Terapeuta|Jornalista|Turism�logo|Enfermeiro|Psic�logo|Bibliotec�rio|Arquivista|Professor))(,| [a-z ]{1,8}[^A-Z]|\\.|/|\\s*<)";

		String regexEncargo = "(( |\"|\\(|>)(Taxa(s)?|Imposto(s)?|Tributo(s)?|Contribui�(�o|�es)|Empr�stimo(s)? Compuls�rio(s)?|Financiamento(s)?|Encargo(s)?) ((destinad(o|a)(s)? |sobre |relativ(o|a)(s)? |com |para )?(de |do |dos |da |das |� |�s |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na |para )?[A-Z��������]([A-Z�����������]+|[a-z������������]+)(, | - | (e )?)?)+(\\s*(-|/)\\s*[A-Z]([A-Z�����������&]+|[a-z������������&]+))*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexIndice = "(( |\"|\\(|>)(�ndice) (((destinad(o|a)(s)? |sobre |relativ(o|a)(s)? |com |para )?(de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na )?[A-Z��������]([A-Z�����������]+|[a-z������������]+)(, | - | (e )?)?))+(\\s*(-|/)\\s*[A-Z]([A-Z�����������&]+|[a-z������������&]+))*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexLocal = "(( |\"|\\(|>)(S�tio(s)?|Monumento(s)?|Riacho(s)?|Parque(s)|Pra�a(s)?|Serra(s)?|Mata(s)?|Floresta(s)?|Planalto(s)?|Depress(�o|�es)) ((de |do |dos |da |das |� |a |ao |aos |pelo |pelos |pelas |pela |por |em |no |nos |nas |na )?[A-Z��������]([A-Z�����������]+|[a-z������������]+)(, | - | (e )?)?|[0-9 e]+)+(\\s*(-|/)\\s*[A-Z][A-Za-z\\Q������������������������&\\E]+)*)\\s*(,|<|\\Q.\\E|;|:|\"|\\Q)\\E|\\Q(\\E| [a-z���������])";

		String regexSite = " www(\\d)?((\\.[a-z]+))+(/( )?([A-Za-z]+)?)?";

		String regexEnd = " <INF_EMP>([0-9A-Za-z������������������������\\Q() ,���-.;/'\"\\E]+), com CNPJ(/MF)?";

		String regexEndNormal = " (estabelecid|situad|localizad)(a na|o no)"; 

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

		Pattern padraoDoc = Pattern.compile(regexDoc);

		Pattern padraoDoc2 = Pattern.compile(regexDoc2);

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



		Hashtable<String, String> tiposDocumentos = new Hashtable<>();

		tiposDocumentos.put("Lei", "LE");

		tiposDocumentos.put("Lei Complementar", "LC");

		tiposDocumentos.put("Lei Federal", "LF");

		tiposDocumentos.put("Decreto", "DE");

		tiposDocumentos.put("Decreto-Lei", "DL");

		tiposDocumentos.put("Decreto Lei", "DL");

		tiposDocumentos.put("Decreto-Lei Federal", "DLF");

		tiposDocumentos.put("Decreto Lei Federal", "DLF");

		tiposDocumentos.put("Resolu��o", "Re");

		tiposDocumentos.put("Emenda Constitucional", "EC");

		tiposDocumentos.put("Ad Referendum", "AR");

		tiposDocumentos.put("Of�cio", "OF");

		tiposDocumentos.put("Parecer", "PAR");

		tiposDocumentos.put("Parecer Conjunto", "PC");

		tiposDocumentos.put("Portaria", "Pt");

		tiposDocumentos.put("Portaria Conjunta", "PtC");

		tiposDocumentos.put("Ato", "At");

		tiposDocumentos.put("Ato Declarat�rio", "AD");

		tiposDocumentos.put("Ata", "ATA");

		tiposDocumentos.put("Ajuste", "Aj");

		tiposDocumentos.put("Termo", "Te");

		tiposDocumentos.put("Lei Complementar Federal", "LCF");

		tiposDocumentos.put("Protocolo", "PROT");

		tiposDocumentos.put("Lei Or�ament�ria Anual", "LOA");





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

		} catch (IOException e1) {

			System.out.println("Arquivo n�o criado.");

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

		//AQUI COME�A A REMO��O DA FORMATA��O





		for (int i = 0; i < anosDecretos.length; i++){

			File a = new File("decretos" + anosDecretos[i]);



			try {

				bwb.write("\nDados para o ano de " + anosDecretos[i]);

				bwb.newLine();

				bwc.write("\nDados para o ano de " + anosDecretos[i]);

				bwc.newLine();

				bwd.write("\nDados para o ano de " + anosDecretos[i]);

				bwd.newLine();

				bwf.write("\nDados para o ano de " + anosDecretos[i]);

				bwf.newLine();

			} catch (IOException e1) {

				System.out.println("N�o conseguiu escrever no documento");

			}



			arquivoPasta = new File(caminhoDecretos + anosDecretos[i] + "\\" + pastaDestino);

			//System.out.println(arquivoPasta);

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

			quantidadeTabelaOrcamentarias.put("Disp�e", 0);

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

						decretoSaida += "<FIN>\n";

						if (matcher.group(1) != null) decretoSaida +=  matcher.group(1).trim();

						decretoSaida += matcher.group(2).trim() + matcher.group(4).trim() + " " +

								matcher.group(5).trim() +  "\n</FIN>";

					} else if (decretoIntermediario.contains("Pal�cio do Campo")){

						bwarq.write(i + "     " + nomeArquivo + "     " + "FINALMENTES");

						bwarq.newLine();

					}



					matcher = padraoAssinaturas.matcher(decretoIntermediario);

					matcherAuxiliar = padraoAssinaturas2.matcher(decretoIntermediario);



					if (matcherAuxiliar.find()){

						decretoSaida += "<ASS>" + matcherAuxiliar.group(1).trim() + " " 

								+ matcherAuxiliar.group(2);

						if (matcherAuxiliar.group(3) != null) decretoSaida += " " + matcherAuxiliar.group(3);

						decretoSaida += "  " + matcherAuxiliar.group(4).replaceFirst("ERRATA[ A-Z������������]+","").trim() + "</ASS>";

					} else if (matcher.find()){

						decretoSaida += "<ASS>" + matcher.group(1).trim() + " "

								+ matcher.group(2);

						if (matcher.group(3) != null) decretoSaida += " " + matcher.group(3);

						decretoSaida +=  "  " + matcher.group(4).replaceFirst("ERRATA[ A-Z������������]+","").trim() + "</ASS>";

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

								+ matcher.group(2).replaceFirst("(\\Q(ERRATA PUBLICADA NO DI�RIO OFICIAL DE \\E\\d{1,2} DE [A-Z�]{4,10} DE \\d{4}\\Q)\\E|\\Q(REPUBLICADO POR HAVER SA�DO COM INCORRE��O NO ORIGINAL)\\E)" , "").trim()

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



						decretoSaida = decretoSaida.replace(entreTags, textoModificado);

					}



					matcher = padraoAssinatura.matcher(decretoSaida);

					if (matcher.find()){

						entreTags = matcher.group(1);

						segmentosConsAss = entreTags.split("( ){2,}");

						textoModificado = "";

						nomePessoa = segmentosConsAss[0].replaceFirst("Governador do Estado|GOVERNADOR DO ESTADO", "").trim();

						textoModificado += "<ASS_GOV>" + nomePessoa + "</ASS_GOV> " + segmentosConsAss[0].replaceFirst(nomePessoa, "").trim();



						if (nomePessoas.containsKey(nomePessoa)){

							nomePessoas.replace(nomePessoa, nomePessoas.get(nomePessoa) + 1);

						} else {

							nomePessoas.put(nomePessoa, 1);

						}



						for (int k = 1; k < segmentosConsAss.length; k++){

							//System.out.println(segmentosConsAss[k]);

							nomePessoa = segmentosConsAss[k].trim();

							textoModificado += "<ASS_OUTRO>" + nomePessoa + "</ASS_OUTRO>\n";

							if (nomePessoas.containsKey(nomePessoa)){

								nomePessoas.replace(nomePessoa, nomePessoas.get(nomePessoa) + 1);

							} else {

								nomePessoas.put(nomePessoa, 1);

							}

						}

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

					}



					decretoSaida = decretoSaida.replace("table>", "TBL>");



					matcher = padraoLeis.matcher(decretoSaida);

					while (matcher.find() && (matcher.group(7) != null  || matcher.group(9) != null)){ //&& (matcher.group(6) != null || matcher.group(10) != null || matcher.group(11) != null)){

						entreTags = matcher.group(1);

						String tipoDoc = tiposDocumentos.get(matcher.group(3).trim());

						textoModificado = " <" +  tipoDoc;

						//	System.out.println("Leis1 " + entreTags);



						if (matcher.group(9) == null && matcher.group(7) != null){

							if (matcher.group(7).contains("/")){

								textoModificado += " numeracao=" + matcher.group(7).split("/")[0] + " ano=" + matcher.group(7).split("/")[1];

							} else{

								textoModificado += " numeracao=" + matcher.group(7);

							}



						} else if(matcher.group(7) == null && matcher.group(9) != null){

							textoModificado += " data=" + (matcher.group(12).length() < 2? "0" + matcher.group(12) : matcher.group(12))

									+ "-";

							if  (matcher.group(14).contains(".") || matcher.group(14).contains("/") || matcher.group(14).contains("-")){

								textoModificado += (matcher.group(15).length() < 2? "0" + matcher.group(15): matcher.group(15)) + "-" + matcher.group(17);

							} else{

								textoModificado += numeracaoMeses.get(matcher.group(15).toLowerCase()) + "-" + matcher.group(17);

							}

						} else if (matcher.group(7) != null && matcher.group(15) != null){ // 12 � dia, 15 � m�s, 17 ano

							textoModificado += " numeracao=" + matcher.group(8);

							textoModificado += " data=" + (matcher.group(14).length() < 2? "0" + matcher.group(14) : matcher.group(14))

									+ "-";

							if  (matcher.group(14).contains(".") || matcher.group(14).contains("/") || matcher.group(14).contains("-")){

								textoModificado += (matcher.group(15).length() < 2? "0" + matcher.group(15): matcher.group(15)) + "-" + matcher.group(17);

							} else{

								textoModificado += numeracaoMeses.get(matcher.group(15).toLowerCase()) + "-" + matcher.group(17);

							}

						} else if (matcher.group(7) != null && matcher.group(17) != null){

							textoModificado += " numeracao=" + matcher.group(7);

							textoModificado += " ano=" + matcher.group(17);

						}



						textoModificado+= ">" + matcher.group(1).trim() + "</" + tipoDoc + ">";

						if (textoModificado.contains(",<")){

							textoModificado.replace(",</" + tipoDoc +  ">", "</" + tipoDoc +  ">,");

						} else if (textoModificado.contains(";<")){

							textoModificado.replace(";</" + tipoDoc +  ">", "</" + tipoDoc +  ">;");

						} else if (textoModificado.contains(":<")){

							textoModificado.replace(":</" + tipoDoc +  ">", "</" + tipoDoc +  ">:");

						} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

							textoModificado.replace(".</" + tipoDoc +  ">", "</" + tipoDoc +  ">.");

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



					matcher = padraoEmpresaLTDA.matcher(decretoSaida);

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

						}		

					}



					matcher = padraoEmpresaSA.matcher(decretoSaida);

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

						}	

					}



					matcher = padraoEmpresaNaoSALTDA.matcher(decretoSaida);

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

						}				

					}



					matcher = padraoEmpresaNaoSALTDASemSigla.matcher(decretoSaida);

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

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

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EMP>", "</EMP>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

						}			

					}



					matcher = padraoUnidade.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						//sem U mesmo

						if (!(matcher.group(1).contains("nidade Federativa")||

								matcher.group(1).contains("nidade da Federa��o")||

								matcher.group(1).contains("nidade do Munic�pio")||

								matcher.group(1).contains("nidade Municipal") ||

								matcher.group(1).contains("Unidade Escolar"))){

							entreTags = matcher.group(1);



							if (entreTags.charAt(0) == '\"'){

								entreTags = entreTags.replace("\"", "");

								textoModificado = "<UNI>";

							} else if (entreTags.charAt(0) == '('){ 

								entreTags = entreTags.replaceFirst("\\Q(\\E", "");

								textoModificado = "<UNI>";					

							} else if (entreTags.charAt(0) == '>') {

								entreTags = entreTags.replaceFirst(">", "");

								textoModificado = "<UNI>";

							} else {

								textoModificado = " <UNI>";

							}



							if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

								if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

									entreTags = entreTags.split(" - ")[0];

								}

							}

							textoModificado += entreTags.trim() + "</UNI>";

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

						}

					}



					matcher = padraoGrupo.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<GRUP>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<GRUP>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<GRUP>";

						} else {

							textoModificado = " <GRUP>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}

						textoModificado += entreTags.trim() + "</GRUP>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoSecretaria2.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						textoModificado = " <SECR>" + matcher.group(1).trim() + "</SECR>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoSecretaria.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<SECR>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<SECR>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<SECR>";

						} else {

							textoModificado = " <SECR>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}



						textoModificado += entreTags.trim() + "</SECR>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

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

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<INST>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<INST>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<INST>";

						} else {

							textoModificado = " <INST>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}



						textoModificado +=  entreTags.trim() + "</INST>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoRegime.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){

							if (entreTags.charAt(0) == '\"'){

								entreTags = entreTags.replace("\"", "");

								textoModificado = "<REG>";

							} else if (entreTags.charAt(0) == '('){ 

								entreTags = entreTags.replaceFirst("\\Q(\\E", "");

								textoModificado = "<REG>";					

							} else if (entreTags.charAt(0) == '>') {

								entreTags = entreTags.replaceFirst(">", "");

								textoModificado = "<REG>";

							} else {

								textoModificado = " <REG>";

							}



							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}



							textoModificado += entreTags.trim() + "</REG>";

							if (textoModificado.contains(",<")){

								textoModificado = textoModificado.replace(",</REG>", "</REG>,");

							} else if (textoModificado.contains(";<")){

								textoModificado = textoModificado.replace(";</REG>", "</REG>;");

							} else if (textoModificado.contains(":<")){

								textoModificado = textoModificado.replace(":</REG>", "</REG>:");

							} else if (textoModificado.contains(".<")){

								textoModificado = textoModificado.replace(".</REG>", "</REG>.");

							}

							if (textoModificado.split("</CAR>").length != textoModificado.split("<CAR>").length){

								textoModificado = textoModificado.replace("</CAR></REG>", "</REG></CAR>");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

						}

					}



					matcher = padraoFundo.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){

							if (entreTags.charAt(0) == '\"'){

								entreTags = entreTags.replace("\"", "");

								textoModificado = "<FUN>";

							} else if (entreTags.charAt(0) == '('){ 

								entreTags = entreTags.replaceFirst("\\Q(\\E", "");

								textoModificado = "<FUN>";					

							} else if (entreTags.charAt(0) == '>') {

								entreTags = entreTags.replaceFirst(">", "");

								textoModificado = "<FUN>";

							} else {

								textoModificado = " <FUN>";

							}



							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}



							textoModificado += entreTags.trim() + "</FUN>";

							if (textoModificado.contains(",<")){

								textoModificado = textoModificado.replace(",</FUN>", "</FUN>,");

							} else if (textoModificado.contains(";<")){

								textoModificado = textoModificado.replace(";</FUN>", "</FUN>;");

							} else if (textoModificado.contains(":<")){

								textoModificado = textoModificado.replace(":</FUN>", "</FUN>:");

							} else if (textoModificado.contains(".<")){

								textoModificado = textoModificado.replace(".</FUN>", "</FUN>.");

							}

							if (textoModificado.split("</CAR>").length != textoModificado.split("<CAR>").length){

								textoModificado = textoModificado.replace("</CAR></FUN>", "</FUN></CAR>");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

						}

					}



					matcher = padraoGratificacao.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){

							if (entreTags.charAt(0) == '\"'){

								entreTags = entreTags.replace("\"", "");

								textoModificado = "<GRAT>";

							} else if (entreTags.charAt(0) == '('){ 

								entreTags = entreTags.replaceFirst("\\Q(\\E", "");

								textoModificado = "<GRAT>";					

							} else if (entreTags.charAt(0) == '>') {

								entreTags = entreTags.replaceFirst(">", "");

								textoModificado = "<GRAT>";

							} else {

								textoModificado = " <GRAT>";

							}



							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}



							textoModificado += entreTags.trim() + "</GRAT>";

							if (textoModificado.contains(",<")){

								textoModificado = textoModificado.replace(",</GRAT>", "</GRAT>,");

							} else if (textoModificado.contains(";<")){

								textoModificado = textoModificado.replace(";</GRAT>", "</GRAT>;");

							} else if (textoModificado.contains(":<")){

								textoModificado = textoModificado.replace(":</GRAT>", "</GRAT>:");

							} else if (textoModificado.contains(".<")){

								textoModificado = textoModificado.replace(".</GRAT>", "</GRAT>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

						}

					}





					matcher = padraoServico2.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						textoModificado = " <SERV>" + matcher.group(1).trim() + "</SERV>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoServico.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<SERV>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<SERV>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<SERV>";

						} else {

							textoModificado = " <SERV>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}

						textoModificado += entreTags.trim() + "</SERV>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoCargo.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(2);

						String aux = (matcher.group(1) != null? matcher.group(1): "2");						

						if (!(aux.contains("nidade")||aux.contains("omiss�o")

								||aux.contains("omit�")||aux.contains("lano")

								|| aux.contains("Nota") || aux.contains("Or�amento"))){ //(C|c)omit�, (c|C)omiss�o...



							if (entreTags.charAt(0) == '\"'){

								entreTags = entreTags.replace("\"", "");

								textoModificado = "<CAR>";

							} else if (entreTags.charAt(0) == '('){ 

								entreTags = entreTags.replaceFirst("\\Q(\\E", "");

								textoModificado = "<CAR>";					

							} else if (entreTags.charAt(0) == '>') {

								entreTags = entreTags.replaceFirst(">", "");

								textoModificado = "<CAR>";

							} else {

								textoModificado = " <CAR>";

							}



							if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

								if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

									entreTags = entreTags.split(" - ")[0];

								}

							}

							textoModificado += entreTags.trim() + "</CAR>";

							if (textoModificado.contains(",<")){

								textoModificado = textoModificado.replace(",</CAR>", "</CAR>,");

							} else if (textoModificado.contains(";<")){

								textoModificado = textoModificado.replace(";</CAR>", "</CAR>;");

							} else if (textoModificado.contains(":<")){

								textoModificado = textoModificado.replace(":</CAR>", "</CAR>:");

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</CAR>", "</CAR>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

						}

					}



					matcher = padraoCargo2.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(2);

						String aux = (matcher.group(1) != null? matcher.group(1): "2");

						if (!(aux.contains("nidade")||aux.contains("omiss�o")

								||aux.contains("omit�")||aux.contains("lano")

								|| aux.contains("ota"))){

							textoModificado = " <CAR>" + matcher.group(2).trim() + "</CAR>";

							if (textoModificado.contains(",<")){

								textoModificado = textoModificado.replace(",</CAR>", "</CAR>,");

							} else if (textoModificado.contains(";<")){

								textoModificado = textoModificado.replace(";</CAR>", "</CAR>;");

							} else if (textoModificado.contains(":<")){

								textoModificado = textoModificado.replace(":</CAR>", "</CAR>:");

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</CAR>", "</CAR>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

						}

					}



					matcher = padraoEvento.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<EV>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<EV>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<EV>";

						} else {

							textoModificado = " <EV>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}

						if (!entreTags.equalsIgnoreCase("Congresso Nacional")){

							textoModificado += entreTags.trim() + "</EV>";

							if (textoModificado.contains(",<")){

								textoModificado = textoModificado.replace(",</EV>", "</EV>,");

							} else if (textoModificado.contains(";<")){

								textoModificado = textoModificado.replace(";</EV>", "</EV>;");

							} else if (textoModificado.contains(":<")){

								textoModificado = textoModificado.replace(":</EV>", "</EV>:");

							} else if (textoModificado.contains(".<") && !(textoModificado.contains("LTDA.<") || textoModificado.contains("S.A.<"))){

								textoModificado = textoModificado.replace(".</EV>", "</EV>.");

							}

							decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

						}

					}

					/*

					if (nomeArquivo.contains("42.151")){

						System.out.println("ev");

					}

					 */

					matcher = padraoEncargo.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<ENC>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<ENC>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<ENC>";

						} else {

							textoModificado = " <ENC>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2 

								&& !entreTags.contains("CONFINS")){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}

						textoModificado += entreTags.trim() + "</ENC>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoIndice.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<IND>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<IND>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<IND>";

						} else {

							textoModificado = " <IND>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}

						textoModificado += entreTags.trim() + "</IND>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoSistema.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

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

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<PROG>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<PROG>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<PROG>";

						} else {

							textoModificado = " <PROG>";

						}





						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2 

								&& !entreTags.contains("Conex�o Cidad�")){



							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}			

						}



						textoModificado += entreTags.trim() + "</PROG>";

						if (textoModificado.contains(",<")){

							textoModificado = textoModificado.replace(",</PROG>", "</PROG>,");

						} else if (textoModificado.contains(";<")){

							textoModificado = textoModificado.replace(";</PROG>", "</PROG>;");

						} else if (textoModificado.contains(":<")){

							textoModificado = textoModificado.replace(":</PROG>", "</PROG>:");

						} else if (textoModificado.contains(".<")){

							textoModificado = textoModificado.replace(".</PROG>", "</PROG>.");

						}

						if (textoModificado.split("</CAR>").length != textoModificado.split("<CAR>").length){

							textoModificado = textoModificado.replace("</CAR></PROG>", "</PROG></CAR>");

						}

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

					}



					matcher = padraoPremio.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<PREM>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<PREM>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<PREM>";

						} else {

							textoModificado = " <PREM>";

						}

						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){



							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}



						}

						textoModificado += entreTags.trim() + "</PREM>";

						if (textoModificado.contains(",<")){

							textoModificado = textoModificado.replace(",</PREM>", "</PREM>,");

						} else if (textoModificado.contains(";<")){

							textoModificado = textoModificado.replace(";</PREM>", "</PREM>;");

						} else if (textoModificado.contains(":<")){

							textoModificado = textoModificado.replace(":</PREM>", "</PREM>:");

						} else if (textoModificado.contains(".<")){

							textoModificado = textoModificado.replace(".</PREM>", "</PREM>.");

						}

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoPartido.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<PART>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<PART>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<PART>";

						} else {

							textoModificado = " <PART>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}

						textoModificado += entreTags.trim() + "</PART>";

						if (textoModificado.contains(",<")){

							textoModificado = textoModificado.replace(",</PART>", "</PART>,");

						} else if (textoModificado.contains(";<")){

							textoModificado = textoModificado.replace(";</PART>", "</PART>;");

						} else if (textoModificado.contains(":<")){

							textoModificado = textoModificado.replace(":</PART>", "</PART>:");

						} else if (textoModificado.contains(".<")){

							textoModificado = textoModificado.replace(".</PART>", "</PART>.");

						}

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoEdificacao.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<ED>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<ED>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<ED>";

						} else {

							textoModificado = " <ED>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}



						textoModificado += entreTags.trim() + "</ED>";

						if (textoModificado.contains(",<")){

							textoModificado = textoModificado.replace(",</ED>", "</ED>,");

						} else if (textoModificado.contains(";<")){

							textoModificado = textoModificado.replace(";</ED>", "</ED>;");

						} else if (textoModificado.contains(":<")){

							textoModificado = textoModificado.replace(":</ED>", "</ED>:");

						} else if (textoModificado.contains(".<")){

							textoModificado = textoModificado.replace(".</ED>", "</ED>.");

						}

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);			

					}



					matcher = padraoDoc2.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(0);

						textoModificado = " <DOC>" + matcher.group(0).trim() + "</DOC>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

					}



					matcher = padraoDoc.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);



						if (entreTags.charAt(0) == '\"'){

							entreTags = entreTags.replace("\"", "");

							textoModificado = "<DOC>";

						} else if (entreTags.charAt(0) == '('){ 

							entreTags = entreTags.replaceFirst("\\Q(\\E", "");

							textoModificado = "<DOC>";					

						} else if (entreTags.charAt(0) == '>') {

							entreTags = entreTags.replaceFirst(">", "");

							textoModificado = "<DOC>";

						} else {

							textoModificado = " <DOC>";

						}



						if (entreTags.contains(" - ") && entreTags.split(" - ").length >=2){								

							if (entreTags.trim().charAt(0) != entreTags.split(" - ")[1].charAt(0)){

								entreTags = entreTags.split(" - ")[0];

							}

						}

						textoModificado += entreTags.trim() + "</DOC>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags + "\\E", textoModificado);

					}





					matcher = padraoData.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(nomeArquivo);

						entreTags = matcher.group(1);

						textoModificado = " <DAT>" + matcher.group(1).trim() + "</DAT>";

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

						entreTags = matcher.group(3);

						textoModificado = " <INF_EMP>" + matcher.group(3).trim() + "</INF_EMP>";

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

						entreTags = matcher.group(5);

						textoModificado = " <MUN>" + matcher.group(5).trim() + "</MUN>";

						if (textoModificado.contains(",")){

							textoModificado = textoModificado.replaceAll(", ", "</MUN>,").replaceAll(">,", ">, <MUN>").replaceFirst(" e ", "</MUN> e <MUN>");

							textoModificado = textoModificado.replace(",</MUN>", "</MUN>,");

						} else if (textoModificado.contains(";<")){

							textoModificado = textoModificado.replace(";</MUN>", "</MUN>;");

						} else if (textoModificado.contains(":<")){

							textoModificado = textoModificado.replace(":</MUN>", "</MUN>:");

						} else if (textoModificado.contains(".<")){

							textoModificado = textoModificado.replace(".</MUN>", "</MUN>.");

						}

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

					}



					matcher = padraoLocal.matcher(decretoSaida);

					while (matcher.find()){

						entreTags = matcher.group(1);

						textoModificado = " <LOC>" + matcher.group(1).trim() + "</LOC>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

					}



					matcher = padraoSite.matcher(decretoSaida);

					while (matcher.find()){

						//System.out.println(matcher.group(1));

						entreTags = matcher.group(0);

						textoModificado = " <SITE>" + matcher.group(0).trim() + "</SITE>";

						decretoSaida = decretoSaida.replaceFirst("\\Q" + entreTags +"\\E", textoModificado);

					}



					matcher = padraoAnexo.matcher(decretoSaida);

					textoModificado = "";

					//caso volte a usar o matcherAuxiliar para pegar dinheiro

					//matcherAuxiliar = padraoDinheiro.matcher(entreTags);



					if (matcher.find()){

						quantidadeDecretosAnexos.containsKey(tipo);

						quantidadeDecretosAnexos.put(tipo, quantidadeDecretosAnexos.get(tipo) + 1);

						entreTags = matcher.group(1);



						if (entreTags.startsWith("ANEXO �NICO")){

							if (verificarSeTabela(entreTags, tipo) == true){

								if (entreTags.contains("CR�DITO SUPLEMENTAR")){

									entreTags = entreTags.replace("<TBL>", "<TBL tipo=credito_supl>");

								} else if (entreTags.contains("ANULA��O DE DOTA��O")){

									entreTags = entreTags.replace("<TBL>", "<TBL tipo=anulacao_dot>");

								} else if (entreTags.contains("EXCESSO DE ARRECADA��O")){

									entreTags = entreTags.replace("<TBL>", "<TBL tipo=excesso_arrec>");

								} else {

									entreTags = entreTags.replace("<TBL>", "<TBL tipo=outro>");

								}



								String[] segmentosLinha = entreTags.split("</tr>");

								for(int l = 3; l < segmentosLinha.length; l++){

									if (entreTags.contains("ANULA��O DE DOTA��O") || 

											entreTags.contains("CR�DITO SUPLEMENTAR") || 

											entreTags.contains("EXCESSO DE ARRECADA��O")){

										System.out.println(nomeArquivo + " entrou aqui");

										System.out.println(segmentosLinha[l]);

										segmentosLinha[l] = tabelaReformulada(segmentosLinha[l]);

										System.out.println(segmentosLinha[l]);

									}

								}

							}



							if (entreTags.contains("MEMORIAL")){

								textoModificado = "<MEMO>" + entreTags + "</MEMO>";

							} else if (entreTags.contains("PLANO")){

								textoModificado += "<PLAN>" + entreTags.trim() + "</PLAN>";

							}else if (entreTags.contains("FORMUL�RIO")){

								textoModificado += "<FORM>" + entreTags.trim() + "</FORM>";

							} else if (entreTags.contains("C�DIGO")){

								textoModificado += "<COD>" + entreTags.trim() + "</COD>";

							} else if (entreTags.contains("REGIMENTO")){

								textoModificado += "<REG>" + entreTags.trim() + "</REG>";

							} else if (!entreTags.contains("TBL")){

								textoModificado = "<OUTRO>" + entreTags + "</OUTRO>";

							} else {

								textoModificado = "<TAB>" + entreTags + "</TAB>";					

							} 



							decretoSaida = decretoSaida.replace(entreTags, textoModificado);

							decretoSaida = decretoSaida.replace("<ANEXOS>", "<ANEXOS num_anexos=1>");

						} else {

							segmentosConsAss = entreTags.split("ANEXO [IVX]{1,4}");



							for (int k = 1; k < segmentosConsAss.length; k++){	



								if (segmentosConsAss[k] != "" && verificarSeTabela(segmentosConsAss[k], tipo) == true){

									int linhaInicial = 3;

									if (segmentosConsAss[k].contains("CR�DITO SUPLEMENTAR")){

										segmentosConsAss[k] = segmentosConsAss[k].replace("<TBL>", "<TBL tipo=credito_supl>");

									} else if (segmentosConsAss[k].contains("ANULA��O DE DOTA��O")){

										segmentosConsAss[k] = segmentosConsAss[k].replace("<TBL>", "<TBL tipo=anulacao_dot>");

									} else if (segmentosConsAss[k].contains("EXCESSO DE ARRECADA��O")){

										segmentosConsAss[k] = segmentosConsAss[k].replace("<TBL>", "<TBL tipo=excesso_arrec>");

										linhaInicial = 2;

									} else {

										segmentosConsAss[k] = segmentosConsAss[k].replace("<TBL>", "<TBL tipo=outro>");

									}



									String atualizarSegmento = "";

									String[] segmentosLinha = segmentosConsAss[k].split("</tr>");

									if (segmentosConsAss[k].contains("ANULA��O DE DOTA��O") || 

											segmentosConsAss[k].contains("CR�DITO SUPLEMENTAR") || 

											segmentosConsAss[k].contains("EXCESSO DE ARRECADA��O")){

										for(int l = linhaInicial; l < segmentosLinha.length; l++){

											System.out.println(nomeArquivo + " entrou aqui");

											System.out.println(segmentosLinha[l]);

											atualizarSegmento += tabelaReformulada(segmentosLinha[l]);

											System.out.println(segmentosLinha[l]);

										}



										segmentosConsAss[k] = atualizarSegmento;

									}

								}



								if (segmentosConsAss[k].contains("MEMORIAL")){

									textoModificado += "<MEMO>" + "ANEXO " + k + " " + segmentosConsAss[k].trim() + "</MEMO>";

								} else if (segmentosConsAss[k].contains("PLANO")){

									textoModificado += "<PLAN>" + "ANEXO " + k + " " + segmentosConsAss[k].trim() + "</PLAN>";

								} else if (segmentosConsAss[k].contains("FORMUL�RIO") || segmentosConsAss[k].contains("Eu")||segmentosConsAss[k].contains("----") || segmentosConsAss[k].contains("____")){

									textoModificado += "<FORM>" + "ANEXO " + k + " " + segmentosConsAss[k].trim() + "</FORM>";

								} else if (segmentosConsAss[k].contains("C�DIGO")){

									textoModificado += "<COD>" + "ANEXO " + k + " " + segmentosConsAss[k].trim() + "</COD>";

								} else if (segmentosConsAss[k].contains("REGIMENTO")){

									textoModificado += "<REG>" + "ANEXO " + k + " " + segmentosConsAss[k].trim() + "</REG>";

								} else if (!segmentosConsAss[k].contains("TBL")){

									textoModificado += "<OUT>" + "ANEXO " + k + " " + segmentosConsAss[k].trim() + "</OUT>";

								} else{

									textoModificado += "<TAB>" + "ANEXO " + k + " " + segmentosConsAss[k].trim() + "</TAB>";

								}

							}

							decretoSaida = decretoSaida.replace(entreTags, textoModificado);

							decretoSaida = decretoSaida.replace("<ANEXOS>", "<ANEXOS num_anexos=" + (segmentosConsAss.length - 1) + ">");

						}

					}



					decretoSaida = decretoSaida.replaceAll("&middot;", "\u00B7");

					decretoSaida = decretoSaida.replaceAll("&sup2;","\u00B2");

					decretoSaida = decretoSaida.replaceAll(" m2"," m\u00B2");

					decretoSaida = decretoSaida.replaceAll(" m3"," m\u00B3");

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

			}//fim do for para cada decreto

			try {

				List<String> contador = new ArrayList<>(quantidadeDecretosAnexos.keySet());

				for (int v = 0; v < contador.size(); v++){	

					bwb.write(contador.get(v) + "    " + quantidadeDecretosAnexos.get(contador.get(v)));

					bwb.newLine();

					bwc.write(contador.get(v) + "    " + quantidadeAnexosTabela.get(contador.get(v)));

					bwc.newLine();

					bwd.write(contador.get(v) + "    " + nomePessoas.get(contador.get(v)));

					bwd.newLine();

					bwf.write(contador.get(v) + "    " + quantidadeTabelaOrcamentarias.get(contador.get(v)));

					bwf.newLine();

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

		}  catch (IOException e) {

			System.out.println("Os streams foram fechados corretamente.");

		}

	}



	public static boolean verificarSeTabela(String entreTags, String tipo){

		boolean isTabela = false;

		if (entreTags.contains("TBL")){

			isTabela = true;

			quantidadeAnexosTabela.put(tipo, quantidadeAnexosTabela.get(tipo) + 1);

			System.out.println("Possui uma tabela!");



			if (entreTags.contains("R$")){ //� uma tabela or�ament�ria

				quantidadeTabelaOrcamentarias.put(tipo, quantidadeTabelaOrcamentarias.get(tipo) + 1);

			}

		}

		return isTabela;

	}



	public static String tabelaReformulada (String linha){
		Matcher matcher;
		linha = linha.replaceAll("<(/)?td>", "");
		linha = linha.replaceAll("<(/)?tr>", "");
		linha = linha.replaceAll("<(/)?tbody>", "");
		linha = linha.replaceAll("\t", "  ");

		matcher = Pattern.compile(" \\d{5} - [</>A-Z�����������, -]+ ").matcher(linha);
		while (matcher.find()){
			System.out.println("Tabela sendo reformulada...");
			linha = linha.replaceFirst(matcher.group(0), " <ID_ORG_G>" + matcher.group(0).trim() + "</ID_ORG_G> ");
		}

		matcher = Pattern.compile(" \\d{5} [</>A-Z�����������a-z�����������, -]+ ").matcher(linha);
		while (matcher.find()){
			linha = linha.replaceFirst(matcher.group(0), " <ID_ORG>" + matcher.group(0).trim() + "</ID_ORG> ");
		}

		matcher = Pattern.compile("( ([<A-Z���������][/>a-z����������� .-]+)+):").matcher(linha);
		while (matcher.find()){
			linha = linha.replaceFirst(matcher.group(1), " <TIPO>" + matcher.group(1).trim() + "</TIPO>");
		}

		matcher = Pattern.compile(" \\d{2}\\.\\d{3}\\.\\d{4}\\.\\d{4,5}\\s+(- )?[</>A-Z�����������a-z����������� ]+ ").matcher(linha);
		while (matcher.find()){
			linha = linha.replaceFirst(matcher.group(0), "<AT_PROJ>" + matcher.group(0).trim() + "</AT_PROJ> ");
		}

		matcher = Pattern.compile(" \\d\\.\\d\\.\\d{2}\\.\\d{2}\\s+(- )?[/A-Z�����������a-z�����������, -]+ ").matcher(linha);
		while (matcher.find()){
			linha = linha.replaceFirst(matcher.group(0), " <ORIG>" + matcher.group(0).trim() + "</ORIG> ");
		}
		
		matcher = Pattern.compile(" \\d{4}\\.\\d{2}\\.\\d{2}\\s+(- )?[</>A-Z�����������, -]+ ").matcher(linha);
		while (matcher.find()){
			linha = linha.replaceFirst(matcher.group(0), " <ORIG>" + matcher.group(0).trim() + "</ORIG> ");
		}

		matcher = Pattern.compile("[^0-9.)(>]\\d{4} ").matcher(linha);
		while (matcher.find()){
			linha = linha.replaceFirst(matcher.group(0), "<FON>" + matcher.group(0).trim() + "</FON> ");
		}

		matcher = Pattern.compile(" (\\d{1,3}\\.)?\\d{1,3}\\.\\d{3},\\d{2}").matcher(linha);
		while (matcher.find()){
			linha = linha.replaceFirst(matcher.group(0), " <VAL>" + matcher.group(0).trim() + "</VAL>");
		}

		return linha;
	}

}
