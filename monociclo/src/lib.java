import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class lib {
	private String[] instructions_memory;

	public short extract_bits(short value, int bstart, int blength) {
		short mask = (short) ((1 << blength) - 1);
		return (short) ((value >> bstart) & mask);
	}

	public void instructions_memory_write(short addr, short value) {
		if (addr >= 0 && addr < instructions_memory.length) {
			String binario = Integer.toBinaryString(value);
			instructions_memory[addr] = String.format("%16s", binario).replace(' ', '0');
		} else {
			System.out.println("Endereço fora do limite!");
		}
	}

	public String[] get_instructions_memory() {
		return instructions_memory;
	}

	void load_binary(String binary_name) {
		try {
			FileInputStream fileInputStream = new FileInputStream(binary_name);
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);
			long tamanhoArquivo = fileInputStream.getChannel().size();
			int numShorts = (int) (tamanhoArquivo / 2);
			instructions_memory = new String[numShorts];

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

	String[] decode_instruction(String instruction) {
		String tipo = instruction.substring(0, 1);
		if (tipo.equals("1")) {
			String opcode = instruction.substring(1, 7);
			String destino = instruction.substring(7, 10);
			String operand_1 = instruction.substring(10, 13);
			String operand_2 = instruction.substring(13, 16);
			String[] registers_instruction = {tipo, opcode, destino, operand_1, operand_2};
			return registers_instruction;
		} else {
			String opcode = instruction.substring(1, 3);
			String register = instruction.substring(3, 6);
			String immediate = instruction.substring(6, 16);
			String[] immediate_instruction = {tipo, opcode, register, immediate};
			return immediate_instruction;
		}
	}
}
