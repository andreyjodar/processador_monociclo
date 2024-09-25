public class App {
    private static short[] istruction_memory = new short[numShorts];
    private static int[] registers = new int[8]; // 3 bits por registrador (pode receber imediatos de até 10 bits)
    private static int[] data_memory = new int[8]; // 3 bits (pode receber imediatos de até 10 bits)
    private static int cont_instructions = 0; // contador do endereço de instruções (controla jump)

    public static void main(String[] args) throws Exception {

    }

    public static void call_ula(short[] decoded_instruction) {
        if (decoded_instruction[0] == 1) {

            switch (decoded_instruction[1]) {
                case 0:
                    and(destiny, operand_1, operand_2);
                    break;
                case 1:
                    sub(destiny, operand_1, operand_2);
                    break;
                case 2:
                    mul(destiny, operand_1, operand_2);
                    break;
                case 3:
                    div(destiny, operand_1, operand_2);
                    break;
                case 4:
                    cmp_equal(destiny, operand_1, operand_2);
                    break;
                case 5:
                    cmp_neq(destiny, operand_1, operand_2);
                    break;
                case 6:
                    load(destiny, operand_1, operand_2);
                    break;
                case 7:
                    store(destiny, operand_1, operand_2);
                    break;

                case 63:
                    syscall();
                    break;
                default:
                    break;
            }
        } else {
            switch (decoded_instruction[1]) {
                case 0:
                    jump(address);
                    break;
                case 1:
                    jump_cond(register, address);
                    break;
                case 2:
                    break;
                case 3:
                    mov(register, immediate);
                default:
                    break;
            }
        }
    }

    public static void and(short destiny, short operand_1, short operand_2) {
        registers[destiny] = registers[operand_1] + registers[operand_2];
    }

    public static void sub(short destiny, short operand_1, short operand_2) {
        registers[destiny] = registers[operand_1] - registers[operand_2];
    }

    public static void mul(short destiny, short operand_1, short operand_2) {
        registers[destiny] = registers[operand_1] * registers[operand_2];
    }

    public static void div(short destiny, short operand_1, short operand_2) {
        registers[destiny] = registers[operand_1] / registers[operand_2];
    }

    public static void cmp_equal(short destiny, short operand_1, short operand_2) {
        if (registers[operand_1] == registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void cmp_neq(short destiny, short operand_1, short operand_2) {
        if (registers[operand_1] != registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void load(short destiny, short operand_1) {
        registers[destiny] = data_memory[operand_1];
    }

    public static void store(short destiny, short operand_2) {
        data_memory[destiny] = registers[operand_2];
    }

    public static void syscall() {
        if (registers[0] == 0) {
            System.out.println("Fim do Programa!");
        }
    }

    public static void jump(short address) {
        cont_instructions = address;
    }

    public static void jump_cond(short register, short address) {
        if (registers[register] == 1) {
            cont_instructions = address;
        }
    }

    public static void mov(short register, short immediate) {
        registers[register] = immediate;
    }
}
