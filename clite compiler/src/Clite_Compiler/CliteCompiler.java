package Clite_Compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class CliteCompiler {
	
	//Scanner that receive the file to be compile.
	public static Scanner reader;
	
	//String to help the management of the tokens because of recursion
	public static String auxiliaryStr;
	
	public static void main(String[] args) throws IOException {
		
		//Reading the file path from the user
		Scanner filereader = new Scanner(System.in);
		System.out.print("Enter the file path: ");
		String path = filereader.next();
		
		//Check if it is a C-Lite file
		if(!path.endsWith(".clt")) {
			System.out.println("This is not a C-Lite file.");
			System.exit(0);
        }

		//Reading the file with the scanner
		reader = new Scanner(new BufferedReader(new FileReader(path)));
		
		//Start to derive the file
		if(deriveProgram()){
			System.out.println("The file input has a valid gramar of C-Lite.");
		}
		
		filereader.close();
		reader.close();
	}
	
	//Start to derive from the start symbol.
	public static boolean deriveProgram(){
		
		//String to manage the tokens it is not global because the recursion
		String checkToken;
		
		checkToken = reader.next();
		
		//Looking if the file start with the word: int
		if(!checkToken.equals("int")){
			System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
			System.exit(0);
		}
		
		//Look for the next token. main()
		//Two forms of main follow the examples given in class: main() and main ( ).
		checkToken = reader.next(); //Get the next token
		if(checkToken.equals("main()")){
			//Form: main()
		}
		//Check the other form of main ( )
		else
		{
			if(!checkToken.equals("main")){
				System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
				System.exit(0);
			}
			
			checkToken = reader.next(); //Get the next token
			
			if(!checkToken.equals("(")){
				System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
				System.exit(0);
			}
			
			checkToken = reader.next(); //Get the next token

			if(!checkToken.equals(")")){
				System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
				System.exit(0);
			}
		}
		
		//Look for the next token. {
		checkToken = reader.next(); //Get the next token
		
		if(!checkToken.equals("{")){
			System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
			System.exit(0);
		}
		
		//Derive declarations
		if(!deriveDeclarations()){ 
			return false;
		}
		
		//Derive if there are statements
		if(!deriveStatements(false)){
			return false;
		}
		
		checkToken = auxiliaryStr;
		
		//Look for the next token. }
		if(!checkToken.equals("}")){
			System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
			System.exit(0);
		}else if(reader.hasNext()){
			System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
			System.exit(0);
		}
		
		return true;
	}
	
	//Method that derive the declarations
	public static boolean deriveDeclarations(){
		
		//String to manage the tokens it is not global because the recursion
		String checkToken;
		
		//Variable to verify if there at least one declaration, and check if there are no previous errors in the grammar
		boolean thereAreDeclaration = false;
		
		checkToken = reader.next(); //Get the next token
		
		//Check if there is a data type
		while(checkToken.equals("int") || checkToken.equals("bool") || checkToken.equals("float") || checkToken.equals("char")){
			do{
				checkToken = reader.next(); //Get the next token
				
				//Check the name of a variable
				if(Character.isLetter(checkToken.toCharArray()[0]) && checkToken.matches("[a-zA-Z0-9]*")){
					checkToken = reader.next(); //Get the next token
				}
				else{
					System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
					System.exit(0);
				}
				
				//Check if has [, and his content.
				if(checkToken.equals("[")){
					checkToken = reader.next(); //Get the next token
					if(checkToken.matches("[0-9]*")){
						checkToken = reader.next(); //Get the next token
						if(checkToken.equals("]")){
							checkToken = reader.next(); //Get the next token
						}
						else{
							System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
							System.exit(0);
						}
					}
					else{
						System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
						System.exit(0);
					}
				}
			}while(checkToken.equals(",")); //If comma, check for another variable.
			
			//Check if has ;
			if(!checkToken.equals(";")){
				System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
				System.exit(0);
			}

			//There are at least 1 declaration
			else{
				thereAreDeclaration = true;
				checkToken = reader.next(); //Get the next token
			}
		}
		auxiliaryStr = checkToken;
		
		//There is not declarations, or a previous error of grammar
		if(!thereAreDeclaration){
			System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
			System.exit(0);
		}
		return true;
	}
	
	//Method that derive the statements
	public static boolean deriveStatements(boolean oneRound){
		
		//String to manage the tokens it is not global because the recursion
		String checkToken = auxiliaryStr;
		
		//Looking for the different statements available in the C-lite
		while(checkToken.equals(";") || checkToken.equals("{") || (Character.isLetter(checkToken.toCharArray()[0]) && checkToken.matches("[a-zA-Z0-9]*")) || checkToken.equals("if") || checkToken.equals("while")){

			if(checkToken.equals(";")){
				try{
					checkToken = reader.next(); //Get the next token
				}
				catch(Exception e){
					System.out.println("Error Unexpected");
					return false;
				}
				auxiliaryStr = checkToken;
			}
			//Check for {
			else if(checkToken.equals("{")){
				
				checkToken = reader.next(); //Get the next token
				auxiliaryStr = checkToken;
				//Check statements within the block
				if(deriveStatements(false)){
					checkToken = auxiliaryStr;
					//Check for }
					if(!checkToken.equals("}")){
						System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
						System.exit(0);
					}
					else{
						try{
							checkToken = reader.next(); //Get the next token
						}
						catch(Exception e){
							System.out.println("Error Unexpected");
							return false;
						}
						auxiliaryStr = checkToken;
					}
				}
				else{
					return false;
				}
			}
			//Check for if
			else if(checkToken.equals("if")){
				checkToken = reader.next(); //Get the next token
				//Check for (
				if(checkToken.equals("(")){
					checkToken = reader.next(); //Get the next token
					auxiliaryStr = checkToken;
					//Check if there is an expression
					if(deriveExpression()){
						checkToken = auxiliaryStr;
						//Check for )
						if(checkToken.equals(")")){
							checkToken = reader.next(); //Get the next token
							auxiliaryStr = checkToken;
							if(deriveStatements(true)){
								checkToken = auxiliaryStr;
								//Checking for optional else
								if(checkToken.equals("else")){
									checkToken = reader.next(); //Get the next token
									auxiliaryStr = checkToken;
								}
							}
							else{
								return false;
							}
						}
						else{
							System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
							System.exit(0);
						}
					}
					else{
						return false;
					}
				}
				else{
					System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
					System.exit(0);
				}
			}
			//Checking for while
			else if(checkToken.equals("while")){
				checkToken = reader.next(); //Get the next token
				//Check for (
				if(checkToken.equals("(")){
					checkToken = reader.next(); //Get the next token
					auxiliaryStr = checkToken;
					//Check if there is an expression
					if(deriveExpression()){
						checkToken = auxiliaryStr;
						//Check for closing )
						if(checkToken.equals(")")){
							checkToken = reader.next(); //Get the next token
							auxiliaryStr = checkToken;
							if(!deriveStatements(true)){
								checkToken = auxiliaryStr;
								return false;
							}
							else{
								checkToken = auxiliaryStr;
							}
						}
						else{
							System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
							System.exit(0);
						}
					}
					else{
						return false;
					}
				}
				else{
					System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
					System.exit(0);
				}
			}
			//Check if it is an identifier (assignment)
			else if(Character.isLetter(checkToken.toCharArray()[0]) && checkToken.matches("[a-zA-Z0-9]*")){
				checkToken = reader.next();
				
				//Check if there is [
				if(checkToken.equals("[")){
					checkToken = reader.next(); //Get the next token
					
					auxiliaryStr = checkToken;
					//Check for expression
					if(deriveExpression()){
						checkToken = auxiliaryStr;
						//Check for closing ]
						if(!checkToken.equals("]")){
							System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
							System.exit(0);
						}
						else{
							checkToken = reader.next(); //Get the next token
						}
					}
				}
				//Check for =
				if(!checkToken.equals("=")){
					System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
					System.exit(0);
				}
				
				checkToken = reader.next(); //Get the next token
				auxiliaryStr = checkToken;
				//Check for expression
				if(deriveExpression()){
					checkToken = auxiliaryStr;
					//Check for ending ;
					if(!checkToken.equals(";")){
						System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
						System.exit(0);
					}
					else{
						checkToken = reader.next(); //Get the next token
						auxiliaryStr = checkToken;
					}
				}
			}
			if(oneRound){
				return true;
			}
		}
		return true;
	}
	
	//Method that derive the expression
	public static boolean deriveExpression(){
		
		//String to manage the tokens it is not global because the recursion
		String checkToken = auxiliaryStr;
		
		//Boolean that define if the function its calling twice
		boolean first = true;
		
		do{
			if(!first){
				checkToken = reader.next();
			}
			//UnaryOp
			if(checkToken.equals("-") || checkToken.equals("!")){
				checkToken = reader.next();									
			}
			
			//Literal
			if(checkToken.equals("int") || checkToken.equals("bool") || checkToken.equals("float") || checkToken.equals("char")){
				checkToken = reader.next(); //Get the next token
				//Check for (
				if(!checkToken.equals("(")){
					//Error no "(" found
					System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
					System.exit(0);
				}else{
					checkToken = reader.next(); //Get the next token
					
					auxiliaryStr = checkToken;
					//Check for an expression
					if(deriveExpression()){
						checkToken = auxiliaryStr;
						//Checking for ending )
						if(!checkToken.equals(")")){
							System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
							System.exit(0);
						}
						else{
							checkToken = reader.next(); //Get the next token
							auxiliaryStr = checkToken;
						}
					}
				}
			}
			//Boolean
			else if(checkToken.equals("true") || checkToken.equals("false")){
				checkToken = reader.next(); //Get the next token
				auxiliaryStr = checkToken;
			}
			//Identifier
			else if(Character.isLetter(checkToken.toCharArray()[0]) && checkToken.matches("[a-zA-Z0-9]*")){
				
				//Check if the identifier is not a keyword
				if(checkToken.equals("if") || checkToken.equals("else") || checkToken.equals("while")){
					System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
					System.exit(0);
				}
				
				checkToken = reader.next(); //Get the next token
				
				//Check for optional [
				if(checkToken.equals("[")){
					checkToken = reader.next(); //Get the next token
					auxiliaryStr = checkToken;
					//Checking for an expression
					if(deriveExpression()){
						checkToken = auxiliaryStr;
						//Check for ending ]
						if(!checkToken.equals("]")){
							System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
							System.exit(0);
						}
						//Correctly identifier
						else{
							checkToken = reader.next(); //Get the next token
						}
					}
					else{
						return false;
					}
				}
			}
			//Integer
			else if(checkToken.matches("[0-9]*")){
				checkToken = reader.next(); //Get the next token
				if(checkToken.equals(".")){
					checkToken = reader.next(); //Get the next token
					if(checkToken.matches("[0-9]*")){
						checkToken = reader.next(); //Get the next token
					}
					else{
						System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
						System.exit(0);
					}
				}
				auxiliaryStr = checkToken;
			}
			//Literal Char
			else if(checkToken.equals("'")){
				checkToken = reader.next(); //Get the next token
				if(checkToken.toCharArray().length == 1){
					checkToken = reader.next(); //Get the next token
					if(!checkToken.equals("'")){
						System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
						System.exit(0);
					}
					else{
						checkToken = reader.next(); //Get the next token
						auxiliaryStr = checkToken;
					}
				}
				else{
					System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
					System.exit(0);
				}
			}
			//Checking for optional (
			else if(checkToken.equals("(")){
				
				checkToken = reader.next(); //Get the next token
				auxiliaryStr = checkToken;
				//Check for an expression
				if(deriveExpression()){
					
					checkToken = auxiliaryStr;
					//Check ending )
					if(!checkToken.equals(")")){

						System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
						System.exit(0);
					}
					else{
						checkToken = reader.next(); //Get the next token
						auxiliaryStr = checkToken;
					}
				}
			}
			
			//Error
			else{
				System.out.println("The file is NOT valid for C-Lite Gramar. Error on: " + checkToken);
				System.exit(0);
			}
			if(first){
				first = false;
			}
		//While one of those found, continue derive
		}while(checkToken.equals("||") || checkToken.equals("*") || checkToken.equals("/") || checkToken.equals("%") || checkToken.equals("+") || checkToken.equals("-") || checkToken.equals("<") || checkToken.equals("<=") || checkToken.equals(">") || checkToken.equals(">=") || checkToken.equals("&&") || checkToken.equals("==") || checkToken.equals("!="));
		auxiliaryStr = checkToken;
		return true;
	}

}
