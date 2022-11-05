package Lexico;

import java.util.Map;

import Exceptions.ExceptionLexica;

public class LeitoraCodigo {
	
	char content[];
	int posicao;
	int estado;
	Integer numTabela = 0;
	
	private boolean isDigito(char c) {
		return c >= '0'  && c <= '9';
	}
	
	private boolean isCharMaiusculo(char c) {
		return c >= 'A'  && c <= 'Z';
	}
	
	private boolean isChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A'  && c <= 'Z');
	}
	
	private boolean isOperadorRelacional(char c) {
		return c == '>' || c == '<' || c == '=' ;
	}
	
	private boolean isOperador(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' ;
	}
	
	private boolean isEspaco(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	
	private boolean isPontuacao(char c) {
		return c == ',' || c == ';';
	}
	
	private boolean isDoisPontos(char c) {
		return c == ':';
	}
	
	private boolean isAbreParenteses(char c) {
		return c == '(';
	}
	
	private boolean isFechaParentese(char c) {
		return c == ')';
	}
	
	private boolean isPonto(char c) {
		return c == '.';
	}
	
	private char nextChar() {
		return content[posicao++];
	}
	
	private boolean isFim() {
		return posicao == content.length;
	}
	
	private void volta() {
		posicao--;
	}

	public char[] getContent() {
		return content;
	}

	public void setContent(char[] content) {
		this.content = content;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}
	
	
	public Token nextToken(Map<Integer,String> tabelaDeSimbolos) {
		char charAtual;
		String termo = "";
		Token token = null;
		boolean fimDoArquivo = false;
		if(isFim()) {
			return null;
		}
		
		estado = 0;
		while (!isFim()) {
			charAtual = nextChar();
			
			switch (estado) {
			case 0:
				if(isCharMaiusculo(charAtual)) {
					termo += charAtual;
					estado = 7;
				}
				else if(isDoisPontos(charAtual)) {
					estado = 0;
					termo += charAtual;
				}
				else if(isChar(charAtual)) {
					termo += charAtual;
					estado = 1;
				}
				else if (isDigito(charAtual)) {
					estado = 3;
					termo += charAtual;
				}
				else if (isEspaco(charAtual)) {
					if(termo.equals(":")) {
						estado = 6;
					} else if(termo.equals("=") || termo.equals("<>") || termo.equals("<") || 
							termo.equals("<=") || termo.equals(">=") || termo.equals(">")) {
						estado = 11;
					} else {
						estado = 0;
					}
				}
				else if (isOperadorRelacional(charAtual)) { 
					termo += charAtual;
					if(termo.equals(":=")) {
						estado = 5;
					} else {
						estado = 0;
					}
				}
				else if (isPontuacao(charAtual)) {
					estado = 6;
					termo += charAtual;
				}
				else if (isOperador(charAtual)) {
					estado = 10;
					termo += charAtual;
				}
				else if(isAbreParenteses(charAtual)){
					estado = 12;
					termo += charAtual;
				}
				else if(isFechaParentese(charAtual)) {
					estado = 14;
					termo += charAtual;
				}
				else {
					throw new ExceptionLexica("Símbolo irreconhecido: " + charAtual);
				}
				break;
			case 1:
				if(isChar(charAtual) || isDigito(charAtual)) {
					termo += charAtual;
					estado = 1;
				}
				else if(isEspaco(charAtual)){
					estado = 2;
				}
				else if(isOperadorRelacional(charAtual) || isPontuacao(charAtual) 
						|| isDoisPontos(charAtual) || isOperador(charAtual)){
					estado = 9;
				}
				else {
					throw new ExceptionLexica("Identificador mal formado, termo: " + charAtual);
				}
				break;
			case 2:
				volta();
				token = new Token();
				token.setTipo(Token.TK_IDENTIFICADOR);
				token.setValor(termo);
				if(!tabelaDeSimbolos.containsValue(termo)) {
					numTabela++;
					tabelaDeSimbolos.put(numTabela, termo);
				}
				return token;
			case 3:
				if(isDigito(charAtual)) {
					estado = 3;
					termo += charAtual;
				}
				else if(isPonto(charAtual)) {
					estado = 15;
					termo += charAtual;
				}
				else if(isOperador(charAtual)) {
					estado = 13;
				}
				else if(!isChar(charAtual) && !isPontuacao(charAtual) && 
						!isOperador(charAtual) && !isFechaParentese(charAtual)) {
					estado = 4;
				}
				else if(isPontuacao(charAtual) || isOperador(charAtual) || isFechaParentese(charAtual)) {
					estado = 13;
				}
				else {
					throw new ExceptionLexica("Número irreconhecido: " + charAtual);
				}
				break;
			case 4:
				token = new Token();
				token.setTipo(Token.TK_NUMERO);
				token.setValor(termo);
				volta();
				return token;
			case 5:
				if(termo.equals(":=")) {
					token = new Token();
					token.setTipo(Token.TK_ATRIBUICAO);
					token.setValor(termo);
					volta();
					return token;
				} else {
					token = new Token();
					token.setTipo(Token.TK_OPERADOR);
					token.setValor(termo);
					volta();
					return token;
				}
			case 6:
				token = new Token();
				token.setTipo(Token.TK_PONTUACAO);
				token.setValor(termo);
				volta();
				return token;
			case 7:
				if(isCharMaiusculo(charAtual)) {
					termo += charAtual;
					estado = 7;
				}
				else if(isEspaco(charAtual) || isOperador(charAtual) || isPontuacao(charAtual)){
					estado = 8;
				}
				else {
					throw new ExceptionLexica("Palavra reservada mal formado, termo: " + charAtual);
				}
				break;
			case 8:
				volta();
				token = new Token();
				token.setTipo(Token.TK_PALAVRA_RESERVADA);
				token.setValor(termo);
				return token;
			case 9:
				volta();
				volta();
				token = new Token();
				token.setTipo(Token.TK_IDENTIFICADOR);
				token.setValor(termo);
				if(!tabelaDeSimbolos.containsValue(termo)) {
					numTabela++;
					tabelaDeSimbolos.put(numTabela, termo);
				}
				return token;
			case 10:
				token = new Token();
				token.setTipo(Token.TK_OPERADOR);
				token.setValor(termo);
				volta();
				return token;
			case 11:
				if(termo.equals("<=") || termo.equals(">=") || termo.equals("<>")) {
					token = new Token();
					token.setTipo(Token.TK_OPERADOR_RELACIONAL);
					token.setValor(termo);
					volta();
					return token;
				} else if(termo.equals("=") || termo.equals("<") || termo.equals(">")) {
					token = new Token();
					token.setTipo(Token.TK_OPERADOR_RELACIONAL);
					token.setValor(termo);
					volta();
					return token;
				}
			case 12:
				volta();
				token = new Token();
				token.setTipo(Token.TK_AP);
				token.setValor(termo);
				return token;
			case 13:
				volta();
				volta();
				token = new Token();
				token.setTipo(Token.TK_NUMERO);
				token.setValor(termo);
				return token;
			case 14:
				volta();
				token = new Token();
				token.setTipo(Token.TK_FP);
				token.setValor(termo);
				return token;
			case 15:
				if(isDigito(charAtual)) {
					estado = 16;
				} else {
					throw new ExceptionLexica("Eperado um número após ponto flutuante: " + charAtual);
				}
			case 16:
				if(isDigito(charAtual)) {
					estado = 16;
					termo += charAtual;
				} else {
					volta();
					token = new Token();
					token.setTipo(Token.TK_NUMERO);
					token.setValor(termo);
					return token;
				}
			}
		}
		
		String tipoToken = this.verificaTipoUltimoTermo(termo);
		token = new Token();
		if(tipoToken.equals(Token.TK_NUMERO)) {
			token.setTipo(Token.TK_NUMERO);
		} else if (tipoToken.equals(Token.TK_IDENTIFICADOR)) {
			token.setTipo(Token.TK_IDENTIFICADOR);
			if(!tabelaDeSimbolos.containsValue(termo)) {
				numTabela++;
				tabelaDeSimbolos.put(numTabela, termo);
			}
		} else if (tipoToken.equals(Token.TK_PONTUACAO)) {
			token.setTipo(Token.TK_PONTUACAO);
		} else {
			throw new ExceptionLexica("Último termo do código com erro: " + termo);
		}
		
		token.setValor(termo);
		return token;
		
		/*
		 * token = new Token(); token.setTipo(Token.TK_PALAVRA_RESERVADA);
		 * token.setValor(termo); return token;
		 */
	}
	
	private String verificaTipoUltimoTermo(String termo) {
		if(termo.matches("[0-9]*")) {
			return Token.TK_NUMERO;
		} else if(termo.matches("[a-z][a-zA-Z0-9]*")) {
			return Token.TK_IDENTIFICADOR;
		} else if(termo.equals(";")) {
			return Token.TK_PONTUACAO;
		} else {
			return Token.TK_ERRO;
		}
	}

}
