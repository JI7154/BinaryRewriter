
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.math.*;

public class HexToOp {

	
	static HashMap<String,String> hexMap = new HashMap<>();
	
	public static int isHasOperand(String opCode) {
		if(opCode.contains("PUSH")) {
			return Integer.parseInt((opCode.substring(4)));
		}
		else {
			return -1;
		}
	}
	
	public static void dasm_printer(String HexStr) {
		int index = 0;
		do {
			String hexToken = HexStr.substring(index, index+2); // operator
			try {
				int numOfOperand = isHasOperand(hexMap.get("0x"+hexToken)); // Opcode�� operand ��
				String operator = hexMap.get("0x"+hexToken);
				if(numOfOperand !=-1) {
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
	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("./solcOPCode.txt");
		BufferedReader br = new BufferedReader(fr);
		String OPMapToken = br.readLine();
		
		System.out.println(OPMapToken);
		
		while((OPMapToken = br.readLine())!=null) {
			hexMap.put(OPMapToken.split(" +")[0],OPMapToken.split(" +")[2]);
			
		}
		//opCode Map ����
		
		fr = new FileReader("./sample_sol_Coin.bin");
		br = new BufferedReader(fr);
		FileWriter fw = new FileWriter("./sample_sol_Coin_rewrite.bin",true);
		String HexStr = br.readLine();
		//HexOutput File ����
		System.out.println(HexStr);
		String hexToken = "";
		//Hex�� opcode ������ 1����Ʈ�� ����
		int index = 0;
		boolean keepingRewrite = true;
		String fallbackOPCode = "";
		String fallbackOperand = "";
		String fallbackJumpI = "";
		do {
			hexToken = HexStr.substring(index, index+2); // operator
			try {
				int numOfOperand = isHasOperand(hexMap.get("0x"+hexToken)); // Opcode�� operand ��				
				if(numOfOperand !=-1) {
					String operand = HexStr.substring(index+2,(index+2)+2*numOfOperand); //operand
					if(keepingRewrite) {
						if(numOfOperand ==2) { //push2�϶�
							int nextOpIndex = (index+2)+2*numOfOperand; // ���� Opcode index
							String nextOpHex = HexStr.substring(nextOpIndex, nextOpIndex+2); //���� opcode hex 
							if(hexMap.get("0x"+nextOpHex).contains("JUMPI")) { //����Opcode �� jumpi�϶� rewrite
								fallbackOPCode = hexToken; 
								fallbackOperand = operand;
								fallbackJumpI = nextOpHex;
								fw.append(hexToken); // push2
								fw.append("0"+Long.toHexString(Math.round((long)HexStr.length()/2))); // ���� ����
								keepingRewrite = false;
							}
							else { // ���� opcode�� jumpi�� �ƴҶ� ���ó�� ����
								fw.append(hexToken);
								fw.append(operand);
							}
						}
						else { // push2�� �ƴҶ�
							fw.append(hexToken);
							fw.append(operand);							
						}
					}
					else {
						fw.append(hexToken);
						fw.append(operand);
					}
					index = (index+2)+2*numOfOperand;
				}//push�϶�
				else {
					fw.append(hexToken); 
					index +=2;
				}//push�� �ƴҶ� �׳� opcode�� ����
			}catch(Exception e) {
				fw.append(hexToken);
				index +=2;
				//Hex���� opCode�� ��ġ �ȵ� ��
			}
		}while(index < HexStr.length());
		if(!keepingRewrite) {
			
			fw.append("5b"); // append jumpdest 
			fw.append(fallbackOPCode);
			fw.append(fallbackOperand);
			fw.append(fallbackJumpI);
		}
		
		
		br = new BufferedReader(new FileReader("./sample_sol_Coin_rewrite.bin"));
		System.out.println("rewriter\n"+br.readLine());
		System.out.println(HexStr.length());
		
		dasm_printer(HexStr);
		fr.close();
		br.close();
		fw.close();

	}
}
