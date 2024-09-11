public class App {
    private static String[] instructions_memory; // 16 bits por instrução (tamanho indeterminado)
    private static int[] registers = new int[8]; // 3 bits por registrador (pode receber imediatos de até 10 bits)
    private static int[] data_memory = new int[8]; // 3 bits (pode receber imediatos de até 10 bits)
    private static int cont_instructions = 0;  // contador do endereço de instruções (controla jump)

    public static void main(String[] args) throws Exception {
        try {
            lib biblioteca = new lib();
            biblioteca.load_binary(
                    "C:\\Users\\Andrey\\OneDrive\\Desktop\\processador_monociclo\\monociclo\\bin\\perfect-squares.bin");
            instructions_memory = biblioteca.get_instructions_memory();
            do {
                String[] decoded_instruction = biblioteca.decode_instruction(instructions_memory[cont_instructions]);
                call_ula(decoded_instruction);
                
            } while (cont_instructions < instructions_memory.length);

        } catch (Exception e) {

        }
    }
 
    public static void call_ula(String[] decoded_instruction){
        if(decoded_instruction[0] == "1"){
            
            switch (decoded_instruction[1]) {
                case "000000":
                    and(destiny, operand_1, operand_2);
                    break;
                case "000001":
                    sub(destiny, operand_1, operand_2);
                    break;
                case "000010":
                    mul(destiny, operand_1, operand_2);
                    break;
                case "000011":
                    div(destiny, operand_1, operand_2);
                    break;
                case "000100":
                    cmp_equal(destiny, operand_1, operand_2);
                    break;
                case "000101":
                    cmp_neq(destiny, operand_1, operand_2);
                    break;
                case "000110":
                    load(destiny, operand_1, operand_2);
                    break;
                case "000111":
                    store(destiny, operand_1, operand_2);
                    break;

                case "111111":
                    syscall();
                    break;
                default:
                    break;
            }
        } else {
            switch (decoded_instruction[1]) {
                case "00":
                    jump(address);
                    break;
                case "01":
                    jump_cond(register, address);
                    break;
                case "10":
                    break;
                case "11":
                    mov(register, immediate);
                default:
                    break;
            }
        }
    }

    public static void and(int destiny, int operand_1, int operand_2) {
        registers[destiny] = registers[operand_1] + registers[operand_2];
    }

    public static void sub(int destiny, int operand_1, int operand_2) {
        registers[destiny] = registers[operand_1] - registers[operand_2];
    }

    public static void mul(int destiny, int operand_1, int operand_2) {
        registers[destiny] = registers[operand_1] * registers[operand_2];
    }

    public static void div(int destiny, int operand_1, int operand_2) {
        registers[destiny] = registers[operand_1] / registers[operand_2]; 
    }

    public static void cmp_equal(int destiny, int operand_1, int operand_2) {
        if(registers[operand_1] == registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void cmp_neq(int destiny, int operand_1, int operand_2) {
        if(registers[operand_1] != registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void load(int destiny, int operand_1) {
        registers[destiny] = data_memory[operand_1];
    }

    public static void store(int destiny, int operand_2) {
        data_memory[destiny] = registers[operand_2];
    }

    public static void syscall () {
        if(registers[0] == 0) {
            System.out.println("Fim do Programa!");
        }
    }

    public static void jump(int address) {
        cont_instructions = address;
    }

    public static void jump_cond(int register, int address) {
        if(registers[register] == 1) {
            cont_instructions = address;
        }
    }

    public static void mov(int register, int immediate) {
        registers[register] = immediate;
    }
}
