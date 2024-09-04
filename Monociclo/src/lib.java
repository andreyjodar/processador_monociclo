import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class lib {
	private short[] instructions_memory;

	public short extract_bits(short value, int bstart, int blength) {
		short mask = (short) ((1 << blength) - 1);
		return (short) ((value >> bstart) & mask);
	}

	public void instructions_memory_write(short addr, short value) {
		if (addr >= 0 && addr < instructions_memory.length)
			instructions_memory[addr] = value;
		else {
			System.out.println("Endereço fora do limite!");
		}
	}

	public short[] get_instructions_memory() {
		return instructions_memory;
	}

	void load_binary(String binary_name) {
		try {
			FileInputStream fileInputStream = new FileInputStream(binary_name);
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			long tamanhoArquivo = fileInputStream.getChannel().size();
			int numShorts = (int) (tamanhoArquivo / 2);
			instructions_memory = new short[numShorts];

			for (int i = 0; i < numShorts; i++) {
				int low = dataInputStream.readByte() & 0x000000FF;
				int high = dataInputStream.readByte() & 0x000000FF;
				int value = (low | (high << 8)) & 0x0000FFFF;

				this.instructions_memory_write((short) i, (short) value);
			}

			dataInputStream.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void decode_instruction(short instruction) {
		short tipo = extract_bits(instruction, 0, 1);
		if (tipo == 1) {
			String opcode = "";
			int bstart = 1;

			for (int i = 0; i < 7; i++) {
				opcode += Integer.valueOf(extract_bits(instruction, bstart, bstart + 1));
				bstart += 1;
			}

			short destino = extract_bits(instruction, 7, 10);
			short operand_1 = extract_bits(instruction, 10, 13);
			short operand_2 = extract_bits(instruction, 13, 16);
			System.out.println(tipo + " " + opcode + " " + destino + " " + operand_1 + " " + operand_2);
		} else {
			short opcode = extract_bits(instruction, 1, 3);
			short register = extract_bits(instruction, 3, 6);
			short immediate = extract_bits(instruction, 6, 16);
			System.out.println(tipo + " " + opcode + " " + register + " " + immediate);
		}
	}

	/*
	 * String registers_instruction(String binary_instruction) {
	 * char format = binary_instruction.charAt(0);
	 * String opcode = binary_instruction.substring(1, 6);
	 * String destination = binary_instruction.substring(7, 9);
	 * String operand_1 = binary_instruction.substring(10, 12);
	 * String operand_2 = binary_instruction.substring(13, 15);
	 * return format + "." + opcode + "." + destination + "." + operand_1 + "." +
	 * operand_2;
	 * }
	 * 
	 * String immediate_instruction(String binary_instruction) {
	 * char format = binary_instruction.charAt(0);
	 * String opcode = binary_instruction.substring(1, 2);
	 * String register = binary_instruction.substring(3, 5);
	 * String immediate = binary_instruction.substring(6, 15);
	 * return format + "." + opcode + "." + register + "." + immediate;
	 * }
	 */
}
