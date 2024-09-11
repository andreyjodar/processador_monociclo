public class App {
    private static String[] instructions_memory; // 16 bits por instrução (tamanho indeterminado)
    private int[] registers = new int[8]; // 3 bits por registrador (pode receber imediatos de até 10 bits)
    private int[] data_memory = new int[8]; // 3 bits (pode receber imediatos de até 10 bits)
    private int[] ula = new int[64]; // 64 operações

    public static void main(String[] args) throws Exception {
        try {
            lib biblioteca = new lib();
            biblioteca.load_binary(
                    "C:\\Users\\Aluno\\Downloads\\processador_monociclo-main\\Monociclo\\src\\perfect-squares.bin");
            instructions_memory = biblioteca.get_instructions_memory();

            for (int i = 0; i < instructions_memory.length; i++) {
                biblioteca.decode_instruction(instructions_memory[i]);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
