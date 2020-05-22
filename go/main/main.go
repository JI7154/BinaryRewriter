package main

import (
	"binaryRewriter/opcodes"
	"encoding/hex"
	_ "encoding/hex"
	"fmt"
	"math"
	_ "strconv"
)



func dasm_printer(hexStr string) {
	var index int = 0

	for index < len(hexStr) {

		var hexStrToken string = ""
		hexStrToken = hexStr[index : index+2]                     // string Type hexToken
		hexToken ,_:= hex.DecodeString(hexStrToken)               // string to byte
		opCodeToken := opcodes.OpCode(hexToken[0])                // byte to OpCode(struct)
		var operator string = opcodes.String(opCodeToken) // operator

		switch {
		case opcodes.IsPush(opCodeToken): // operator is Push
			var numOfOperand int = 0
			numOfOperand = int(opCodeToken - 0x5f)
			var operand string = "0x" + hexStr[index+2:index+2+(2*numOfOperand)] // operand

			fmt.Print(operator + " ")
			fmt.Println(operand)
			index += 2 + (2 * numOfOperand)

		case len(opcodes.String(opCodeToken)) == 0:
			fmt.Printf("0x%x \n",hexToken)
			index += 2

		default:
			fmt.Println(operator + " ")
			index += 2

		}

	}

}
//func numOfOperand (hexToken string) int{
//	intHexToken , _ := strconv.Atoi(hexToken)
//	return intHexToken - 0x5f
//}
//
//func getOperand (index int,hexStr string,hexToken string) string{
//	return hexStr[index+2 : (index+2)+2*numOfOperand(hexToken)]
//}
//
//func isOverWritable(keepRewrite bool,hexToken string,index int ,hexStr string) bool{
//	return keepRewrite && numOfOperand(hexToken) ==2 && isNextOpJumpI (index,hexStr)
//}
////func isNextOpJumpI (index int,hexStr string) bool {
////	nextIndex := (index +2) +4
////	nextOpToken := hexStr[nextIndex,nextIndex+2]
////
////	return (nextOpToken == "57")
////}
//
func getRewriteOperandJumpI(hexStr string) string{
	lastAddress := string(math.Float64bits(math.Round(float64(len(hexStr)) / 2)))
	if len(lastAddress) % 2 ==0{
	}else{
		lastAddress = "0" + lastAddress
	}
	return lastAddress
}

//func binRewrite(hexStr string) string {
//
//	//var rewritedStr string = ""
//	var index int = 0
//
//	var keepRewrite bool = true
//	//var fallbackOperand string = ""
//
//
//	for index < len(hexStr) {
//		hexToken := hexStr[index : index+2]
//		decodedHexToken,_ := hex.DecodeString(hexToken)
//		opcode := opcodes.OpCode(decodedHexToken[0])
//
//		if opcodes.IsPush(opcode) {
//			operand := getOperand(index,hexStr,hexToken)
//			var numOfOperand int = Atoi(hexToken[1:]) + 1
//			if(isOverWritable(keepRewrite,hexToken,index,hexStr)){
//				fallbackOperand = operand;
//				rewriteOperand = getRewriteOperandJumpI(hexStr)
//			}
//		}
//	}
//}
func main() {
	fmt.Println("Hello world")
	hexStr := "608060405234801561001057600080fd5b506004361061004c5760003560e01c8063075461721461005157806327e235e31461009b57806340c10f19146100f3578063d0679d3414610141575b600080fd5b61005961018f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6100dd600480360360208110156100b157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506101b4565b6040518082815260200191505060405180910390f35b61013f6004803603604081101561010957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506101cc565b005b61018d6004803603604081101561015757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610277565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60016020528060005260406000206000915090505481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461022557610273565b80600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055505b5050565b80600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410156102c3576103fd565b80600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555080600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055507f3990db2d31862302a685e8086b5755072a6e2b5b780af1ee81ece35ee3cd3345338383604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a15b505056fea264697066735822122050aae312e7504c57c39f448c6507a3db893195624becddfe307ca9a5db0671ac64736f6c63430006060033"
	fmt.Println(math.Round(float64(len(hexStr))/2))
	dasm_printer("608060405234801561001057600080fd5b506004361061004c5760003560e01c8063075461721461005157806327e235e31461009b57806340c10f19146100f3578063d0679d3414610141575b600080fd5b61005961018f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6100dd600480360360208110156100b157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506101b4565b6040518082815260200191505060405180910390f35b61013f6004803603604081101561010957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506101cc565b005b61018d6004803603604081101561015757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610277565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60016020528060005260406000206000915090505481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461022557610273565b80600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055505b5050565b80600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410156102c3576103fd565b80600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555080600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055507f3990db2d31862302a685e8086b5755072a6e2b5b780af1ee81ece35ee3cd3345338383604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a15b505056fea264697066735822122050aae312e7504c57c39f448c6507a3db893195624becddfe307ca9a5db0671ac64736f6c63430006060033")
}