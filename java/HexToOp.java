
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.math.*;

public class HexToOp {


	static HashMap<String,String> hexMap = new HashMap<>(); // key:bin, value:opCode map



	public static void dasm_printer(String HexStr) {
		int index = 0;

		do {
			String hexToken = HexStr.substring(index, index+2); // operator
			try {
				int numOfOperand = Integer.parseInt(hexToken,16) - 0x5f; // Opcode�� operand ��
				String operator = hexMap.get("0x"+hexToken);
				if(isPush(hexToken)) {
					String operand = HexStr.substring(index+2,(index+2)+2*numOfOperand); //operand
					System.out.print(operator+" "); //
					System.out.println("0x"+operand);
					index = (index+2)+2*numOfOperand;
				}//push�϶�
				else {
					System.out.println(operator+" ");
					index +=2;
				}//push�� �ƴҶ�
			}catch(Exception e) {
				System.out.println("0x"+hexToken);
				index +=2;
				//Hex���� opCode�� ��ġ �ȵ� ��
			}
		}while(index < HexStr.length());
	}

	public static void createOpMap() throws IOException {
		FileReader fr = new FileReader("./solcOPCode.txt");
		BufferedReader br = new BufferedReader(fr);
		String OPMapToken = br.readLine();

		System.out.println(OPMapToken);

		while((OPMapToken = br.readLine())!=null) {
			hexMap.put(OPMapToken.split(" +")[0],OPMapToken.split(" +")[2]);
		}
		//opCode Map ����
		fr.close();
		br.close();

	}

	public static boolean isPush(String hexToken) {
		int binaryToken = Integer.parseInt(hexToken,16);
		if(binaryToken > 0x5f && binaryToken < 0x80) {
			return true;
//			return Integer.parseInt((opCode.substring(4)));
		}
		else {
			return false;
		}
	}//opCode �� push���� üũ�ؼ� operand ���� ����

	public static boolean isValidOpcode(String hexToken) {
		return hexMap.get("0x"+hexToken)!= null;
	}

	public static String binary_rewrite_main(String hexStr) {
		String hexToken = "";
		//Hex�� opcode ������ 1����Ʈ�� ����
		String reWritedStr = "";
		int index = 0;
		boolean keepingRewrite = true;
		String fallbackOPCode = "";
		String fallbackOperand = "";
		String fallbackJumpI = "";
		do {
			hexToken = hexStr.substring(index, index+2); // operator
			try {//hexStr�� ������ outOfIndex ó���� ����
//				int numOfOperand = isHasOperand(hexMap.get("0x"+hexToken)); // Opcode�� operand ��
				if(isPush(hexToken)) {
					int numOfOperand = Integer.parseInt(hexToken,16) - 0x5f;
					String operand = hexStr.substring(index+2,(index+2)+2*numOfOperand);
					if(keepingRewrite) {
						if(numOfOperand == 2 ) {
							int nextIndex = (index+2)+4;//2+numOfOperand
							String nextOpToken = hexStr.substring(nextIndex,nextIndex+2);
							if(nextOpToken.equals("57")) { // nextOpToken == jumpI
								fallbackOPCode = hexToken;
								fallbackOperand = operand;
								fallbackJumpI = nextOpToken;
								keepingRewrite = false;
								System.out.println(hexStr.length());
								String lastAddress = Integer.toHexString(Math.round((float)hexStr.length()/2));
								lastAddress = (lastAddress.length()%2)==0 ? lastAddress : "0"+lastAddress; // ¦���ڸ��� binary�� ���ֱ� ����
								reWritedStr += hexToken + lastAddress;
								index = (index+2) + 2*numOfOperand;
							}else {
								reWritedStr += hexToken + operand;
								index = (index+2) + 2*numOfOperand;
							}
						}else {//�Ϲ� Push
							reWritedStr += hexToken + operand;
							index = (index+2) + 2*numOfOperand;
						}
					}
					else {
						reWritedStr += hexToken + operand;
						index = (index+2) + 2*numOfOperand;
					}
				}else if(isValidOpcode(hexToken)) {
					reWritedStr += hexToken;
					index+=2;
				}else {
					reWritedStr += hexToken;
					index+=2;
				}
//				if(numOfOperand !=-1) {
//					String operand = HexStr.substring(index+2,(index+2)+2*numOfOperand); //operand
//					if(keepingRewrite) {
//						if(numOfOperand ==2) { //push2�϶�
//							int nextOpIndex = (index+2)+2*numOfOperand; // ���� Opcode index
//							String nextOpHex = HexStr.substring(nextOpIndex, nextOpIndex+2); //���� opcode hex
//							if(hexMap.get("0x"+nextOpHex).contains("JUMPI")) { //����Opcode �� jumpi�϶� rewrite
//								fallbackOPCode = hexToken;
//								fallbackOperand = operand;
//								fallbackJumpI = nextOpHex;
//								fw.append(hexToken); // push2
//								fw.append("0"+Long.toHexString(Math.round((long)HexStr.length()/2))); // ���� ����
//								keepingRewrite = false;
//							}
//							else { // ���� opcode�� jumpi�� �ƴҶ� ���ó�� ����
//								fw.append(hexToken);
//								fw.append(operand);
//							}
//						}
//						else { // push2�� �ƴҶ�
//							fw.append(hexToken);
//							fw.append(operand);
//						}
//					}
//					else {
//						fw.append(hexToken);
//						fw.append(operand);
//					}
//					index = (index+2)+2*numOfOperand;
//				}//push�϶�
//				else {
//					fw.append(hexToken);
//					index +=2;
//				}//push�� �ƴҶ� �׳� opcode�� ����
			}catch(Exception e) { // outOfindex
				reWritedStr += hexStr.substring(index,hexStr.length()-1); // ������ �� ����� �Ŀ�
				reWritedStr += "5b"+fallbackOPCode+ fallbackOperand+fallbackJumpI;
				System.out.println("reWrite Complete");
				break;
				//Hex���� opCode�� ��ġ �ȵ� ��
			}
		}while(index < hexStr.length());
		System.out.println("reWrited Bin :");
		System.out.println(reWritedStr);
//		if(!keepingRewrite) {
//
//			fw.append("5b"); // append jumpdest
//			fw.append(fallbackOPCode);
//			fw.append(fallbackOperand);
//			fw.append(fallbackJumpI);
//		}


//		br = new BufferedReader(new FileReader("./sample_sol_Coin_rewrite.bin"));
		return reWritedStr;
	}



	public static void main(String[] args) throws IOException {

		createOpMap(); //static Hashmap create


		FileReader fr = new FileReader("./sample_sol_Coin.bin");
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter("./sample_sol_Coin_rewrite.bin",true);
		String hexStr = br.readLine();
		String reWritedBin = binary_rewrite_main(hexStr);
		fw.write(reWritedBin);

//
//		System.out.println("rewriter\n"+br.readLine());
//		System.out.println(HexStr.length());
//
		System.out.println(reWritedBin.length());
		dasm_printer(reWritedBin);
		fr.close();
		br.close();
		fw.close();


	}
}
