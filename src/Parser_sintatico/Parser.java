package Parser_sintatico;

import java.util.Map;

import Exceptions.ExceptionSintatica;
import Lexico.LeitoraCodigo;
import Lexico.Token;

public class Parser {
	
	private LeitoraCodigo scanner;
	private Token tokenAtual;
	private Map<Integer,String> tabelaDeSimbolos;
	
	public Parser(LeitoraCodigo scanner, Map<Integer,String> tabelaDeSimbolos) {
		this.scanner = scanner;
		this.tabelaDeSimbolos = tabelaDeSimbolos;
	}
	
	public void E() {
		T();
		El();
	}
	
	public void El() {
		tokenAtual = scanner.nextToken(tabelaDeSimbolos);

		if(tokenAtual != null) {
			OP();
			T();
			El();
		}
	}
	
	public void T() {
		tokenAtual = scanner.nextToken(tabelaDeSimbolos);
		if(tokenAtual.getTipo() != Token.TK_IDENTIFICADOR  && tokenAtual.getTipo() != Token.TK_NUMERO) {
			throw new ExceptionSintatica("Esperado um ID ou Numero, porém o encontrado foi: " + tokenAtual.getTipo() + " com valor " + tokenAtual.getValor());
		}
	}
	
	public void OP() {
		if(tokenAtual.getTipo() != Token.TK_OPERADOR) {
			throw new ExceptionSintatica("Esperado um operador, porém o encontrado foi: " + tokenAtual.getTipo() + " com valor " + tokenAtual.getValor());
		}
	}
	
	public void P() {
		
	}
	

}
