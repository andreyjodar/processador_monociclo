public class App {
    static short[] instruction_memory;
    static short[] registers = new short[8]; // 3 bits por registrador (pode receber imediatos de até 10 bits)
    static short[] data_memory; // 3 bits (pode receber imediatos de até 10 bits)
    static int program_count = 0; // contador do endereço de instruções (controla jump)
    static lib bib = new lib();

    public static void main(String[] args) throws Exception {
        instruction_memory = bib.load_binary("C:\\Users\\Andrey\\OneDrive\\Desktop\\Faculdade - Engenharia de Software\\2º Ano - Eng. Software\\Arquitetura de Computadores\\Atividades\\NewTesteProcessador\\bin-example\\count.bin");
        data_memory = bib.get_data_memory();
        print_instructions_memory();
        program_count++;
        System.out.println("PC = " + program_count);
        while (program_count < instruction_memory.length) {
            short[] decoded_instruction = instruction_decode(program_count, bib);
            call_ula(decoded_instruction);
            print_data_memory();
            print_registers();
            program_count++;
            System.out.println("PC = " + program_count);
        }
    }

	public static short[] instruction_decode(int address, lib bib) {
		short type = bib.extract_bits(instruction_memory[address], 15, 1);
		if (type == 0) {
			short opcode = bib.extract_bits(instruction_memory[address], 9, 6);
			short destiny = bib.extract_bits(instruction_memory[address], 6, 3);
			short operand_1 = bib.extract_bits(instruction_memory[address], 3, 3);
			short operand_2 = bib.extract_bits(instruction_memory[address], 0, 3);
			short[] decoded_instruction = { type, opcode, destiny, operand_1, operand_2 };
			return decoded_instruction;
		} else {
			short opcode = bib.extract_bits(instruction_memory[address], 13, 2);
			short register = bib.extract_bits(instruction_memory[address], 10, 3);
			short immediate = bib.extract_bits(instruction_memory[address], 0, 10);
			short[] decoded_instruction = { type, opcode, register, immediate };
			return decoded_instruction;
		}
	}

    public static void call_ula(short[] decoded_instruction) {
		if (decoded_instruction[0] == 0) {
			switch (decoded_instruction[1]) {
				case 0:
					add(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
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
                    cmp_less(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
                    break;
                case 7:
                    cmp_greater(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
                    break;
                case 8: 
                    cmp_less_eq(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
                    break;
                case 9:
                    cmp_greater_eq(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
                    break;
                case 10:
                    and(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
                    break;
                case 11:
                    or(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
                    break;
                case 12:
                    xor(decoded_instruction[2], decoded_instruction[3], decoded_instruction[4]);
                    break;
                case 13:
                    // shl();
                    break;
                case 14:
                    // shr();
                    break;
				case 15:
					load(decoded_instruction[2], decoded_instruction[3]);
					break;
				case 16:
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

    public static void add(short destiny, short operand_1, short operand_2) {
        registers[destiny] = (short) (registers[operand_1] + registers[operand_2]);
    }

    public static void sub(short destiny, short operand_1, short operand_2) {
        registers[destiny] = (short) (registers[operand_1] - registers[operand_2]);
    }

    public static void mul(short destiny, short operand_1, short operand_2) {
        registers[destiny] = (short) (registers[operand_1] * registers[operand_2]);
    }

    public static void div(short destiny, short operand_1, short operand_2) {
        registers[destiny] = (short) (registers[operand_1] / registers[operand_2]);
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

    public static void cmp_less(short destiny, short operand_1, short operand_2) {
        if (registers[operand_1] < registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void cmp_greater(short destiny, short operand_1, short operand_2) {
        if (registers[operand_1] > registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void cmp_less_eq(short destiny, short operand_1, short operand_2) {
        if (registers[operand_1] <= registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void cmp_greater_eq(short destiny, short operand_1, short operand_2) {
        if (registers[operand_1] >= registers[operand_2]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void and(short destiny, short operand_1, short operand_2) {
        if(registers[operand_1] == 1 && registers[operand_2] == registers[operand_1]) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void or(short destiny, short operand_1, short operand_2) {
        if(registers[operand_1] == 1 || registers[operand_2] == 1) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void xor(short destiny, short operand_1, short operand_2) {
        if(!((registers[operand_1] == 1 && registers[operand_2] == registers[operand_1]) 
        || (registers[operand_1] == 0 && registers[operand_1] == registers[operand_2]))) {
            registers[destiny] = 1;
        } else {
            registers[destiny] = 0;
        }
    }

    public static void load(short destiny, short operand_1) {
        registers[destiny] = data_memory[registers[operand_1]];
    }

    public static void store(short destiny, short operand_2) {
        data_memory[registers[destiny]] = registers[operand_2];
    }

    public static void syscall() {
        if (registers[0] == 0) {
            System.exit(0);
        } else if (registers[0] == 1) {  // print string service
            
        } else if(registers[0] == 2) {  // newline print service
            System.out.println("");
        } else if (registers[0] == 3) {  // integer print service
            System.out.println((int) registers[1]);
        }
    }

    public static void jump(short address) {
        program_count = address - 1;
    }

    public static void jump_cond(short register, short address) {
        if (registers[register] == 1) {
            program_count = address - 1;
        }
    }

    public static void mov(short register, short immediate) {
        registers[register] = immediate;
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
        int count = 2;
        while (count > 1 && count < bib.extract_bits(instruction_memory[1], 0, 10)) {
            System.out.println("Endereço " + count + ": " + data_memory[count]);
            count++;
        }
        System.out.println("--------------------------------");
    }
    
}
