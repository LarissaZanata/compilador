package Principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Exceptions.ExceptionLexica;
import Exceptions.ExceptionSintatica;
import Lexico.LeitoraCodigo;
import Lexico.Token;
import Parser_sintatico.Parser;

public class Main {

	public static void main(String[] args) {
		Map<Integer,String> tabelaDeSimbolos = new HashMap<Integer,String>();
		System.out.println("Para iniciar, digite o nome e extensão do arquivo que contém o código a ser lido.");
		System.out.println("Exemplo: 'arquivo.ilp' ");
		Scanner in = new Scanner(System.in);
        String nomeArquivo = in.nextLine();
		String conteudoArquivoSemComentarios = "";
		
		/*Lê código do arquivo já removendo linhas de comentários*/
		try{
			BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
	        String linha;
	        while ((linha = br.readLine()) != null) {
	        	if(!linha.isEmpty()) {
	        		String linhaSemEspacosETabs = linha.trim();
	        		if(!linhaSemEspacosETabs.isEmpty()) {
	        			String char_0 =  Character.toString(linhaSemEspacosETabs.charAt(0));
			        	String char_1 =  Character.toString(linhaSemEspacosETabs.charAt(1));
			        	if(char_0.equals("/") && char_1.equals("/")) {
			        		continue;
			        	}else {
			        		conteudoArquivoSemComentarios = conteudoArquivoSemComentarios + linhaSemEspacosETabs + " ";
			        	}
	        		}
	        	}
	        }
		}catch(Exception e) {
	    	System.out.println("Arquivo não encontrado ou com erro: " + e.getMessage());
		}
		
		conteudoArquivoSemComentarios = conteudoArquivoSemComentarios.trim();
		try {
			LeitoraCodigo scanner = new LeitoraCodigo();
			scanner.setContent(conteudoArquivoSemComentarios.toCharArray());
			scanner.setPosicao(0);
			Parser parser = new Parser(scanner, tabelaDeSimbolos);
			
			parser.E();
			
			/*Token token = null;
			do {
				token = scanner.nextToken();
				if(token != null) {
					System.out.println(token);
				}
			} while(token != null);*/
			System.out.println("Expressão aceita!");
			
		} catch (ExceptionLexica e) {
			System.out.println("Erro Léxico: " + e.getMessage());
		} catch (ExceptionSintatica e) {
			System.out.println("Erro Sintatico: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Erro Genérico.");
			System.out.println(e.getClass().getName());
		}
	}

}
