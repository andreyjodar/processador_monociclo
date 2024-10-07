public class App {
    static short[] instruction_memory;
    static int[] registers = new int[8]; // 3 bits por registrador (pode receber imediatos de até 10 bits)
    static int[] data_memory = new int[8]; // 3 bits (pode receber imediatos de até 10 bits)
    static int program_count = 0; // contador do endereço de instruções (controla jump)

    public static void main(String[] args) throws Exception {
        lib bib = new lib();
        instruction_memory = bib.load_binary("C:\\Users\\Andrey\\OneDrive\\Desktop\\Faculdade - Engenharia de Software\\2º Ano - Eng. Software\\Arquitetura de Computadores\\Atividades\\TesteProcessador\\src\\perfect-squares.bin");
        print_instructions_memory();
        while (program_count < instruction_memory.length) {
            short[] decoded_instruction = instruction_decode(program_count, bib);
            call_ula(decoded_instruction);
            System.out.println("PC = " + program_count);
            if(program_count % 5 == 0){
                print_registers();
                print_data_memory();
            }
            program_count++;
        }
    }

	public static short[] instruction_decode(int address, lib bib) {
		short type = bib.extract_bits(instruction_memory[program_count], 15, 1);
		if (type == 0) {
			short opcode = bib.extract_bits(instruction_memory[program_count], 9, 6);
			short destiny = bib.extract_bits(instruction_memory[program_count], 6, 3);
			short operand_1 = bib.extract_bits(instruction_memory[program_count], 3, 3);
			short operand_2 = bib.extract_bits(instruction_memory[program_count], 0, 3);
			short[] decoded_instruction = { type, opcode, destiny, operand_1, operand_2 };
			return decoded_instruction;
		} else {
			short opcode = bib.extract_bits(instruction_memory[program_count], 13, 2);
			short register = bib.extract_bits(instruction_memory[program_count], 10, 3);
			short immediate = bib.extract_bits(instruction_memory[program_count], 0, 10);
			short[] decoded_instruction = { type, opcode, register, immediate };
			return decoded_instruction;
		}
	}

    public static void call_ula(short[] decoded_instruction) {
		if (decoded_instruction[0] == 0) {
			switch (decoded_instruction[1]) {
				case 0:
					and(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
					break;
				case 1:
					sub(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
					break;
				case 2:
					mul(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
					break;
				case 3:
					div(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
					break;
				case 4:
					cmp_equal(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
					break;
				case 5:
					cmp_neq(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
					break;
				case 6:
					load(decoded_instruction[2], decoded_instruction[3]);
					break;
				case 7:
					store(decoded_instruction[3], decoded_instruction[4]);
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
					jump(decoded_instruction[3]);
					break;
				case 1:
					jump_cond(decoded_instruction[2], decoded_instruction[3]);
					break;
				case 2:
					break;
				case 3:
					mov(decoded_instruction[2], decoded_instruction[3]);
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
        if (registers[operand_1] == registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void cmp_neq(int destiny, int operand_1, int operand_2) {
        if (registers[operand_1] != registers[operand_2]) {
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

    public static void syscall() {
        if (registers[0] == 0) {
            System.exit(0);
        }
    }

    public static void jump(int address) {
        program_count = address - 1;
    }

    public static void jump_cond(int register, int address) {
        if (registers[register] == 1) {
            program_count = address - 1;
        }
    }

    public static void mov(int register, int immediate) {
        registers[register] = (int) immediate;
    }

    public static void print_registers() {
        System.out.println("==== Banco de Registradores ====");
        for (int i = 0; i < registers.length; i++) {
            System.out.println("$r" + i + " = " + registers[i]);
        }
        System.out.println("--------------------------------");
    }

    public static void print_instructions_memory() {
        System.out.println("===== Memória de Instruções ====");
        for (int i = 0; i < instruction_memory.length; i++) {
            System.out.println("Instrução " + i + ": " + String.format("%16s", Integer.toBinaryString(instruction_memory[i] & 0xFFFF)).replace(' ', '0'));
        }
        System.out.println("--------------------------------");
    }

    public static void print_data_memory() {
        System.out.println("======= Memória de Dados =======");
        for (int i = 0; i < data_memory.length; i++) {
            System.out.println("Endereço " + i + ": " + data_memory[i]);
        }
        System.out.println("--------------------------------");
    }
    
}
