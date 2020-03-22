package pl.valvar;

import java.io.*;
import java.util.Random;

public class GA_Knapsackj {
    public static int[][] csv_data = new int[1501][3];
    private static Individual bIndividual = new Individual();
    private static Individual sbIndividual = new Individual();
    public static int num_obj = 1500;
    public static int max_cap = 15000;
    public static int max_size = 20000;
    public static String nameofCSV = "kcp.csv";
    public static int pop_size = 1000;
    public static double crossover_rate = 0.35;
    public static double mutation_rate = 0.015;
    public static int check = 0;

    public static void main(String[] args) throws IOException {
        //Generator();
        ReadData();
        Individual[] population = new Individual[pop_size];
        for (int i = 0; i < pop_size; i++) {
            population[i] = new Individual();
            population[i].init();

        }
        for (int i = 0; i < 100; i++) {
            Random rand = new Random();
            Individual[] newPopulation = new Individual[pop_size];
            for (int j = 2; j < pop_size; j++) {
                int i1 = rand.nextInt(pop_size);
                newPopulation[0] = getLargest(population);
                newPopulation[1] = tournament(population,100);
                newPopulation[j] = newPopulation[0].crossover_operator(newPopulation[1]);
                newPopulation[j].mutate();
                newPopulation[j].evaluate();
            }
            population = newPopulation;
            int best_buffer_old = bIndividual.score;
            int best_buffer_new = getLargest(population).score;
            if (best_buffer_new!=best_buffer_old){
                System.out.println("Population " + i);
                System.out.println("Best score:" + bIndividual.score);
                System.out.println();
            }
        }
        System.out.println("Best score : " + getLargest(population).score);
//        for (int j = 0; j < num_obj; j++) {
//            System.out.print(getLargest(population).chromoset[j]);
//        }
    }

    private static int[] RandomGenerator(){
        int[] ret_num = new int[3];
        Random rand = new Random();
        ret_num[0] = rand.nextInt((10*max_cap)/num_obj);   //weight
        ret_num[1] = rand.nextInt((10*max_size)/num_obj);  //size
        ret_num[2] = rand.nextInt(num_obj);                        //cost
        if (ret_num[0]==0)
            ret_num[0]++;
        if (ret_num[1]==0)
            ret_num[1]++;
        if (ret_num[2]==0)
            ret_num[2]++;
        return ret_num;
    }

    public static void Generator() throws IOException {
        int i=0;
        try (PrintWriter writer = new PrintWriter(new File(nameofCSV))) {
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(num_obj));
            sb.append(',');
            sb.append(Integer.toString(max_cap));
            sb.append(',');
            sb.append(Integer.toString(max_size));
            sb.append("\n");
            while(i<num_obj){
                int[] buffer = RandomGenerator();
                sb.append(Integer.toString(buffer[0]));//weight
                sb.append(',');
                sb.append(Integer.toString(buffer[1]));//size
                sb.append(',');
                sb.append(Integer.toString(buffer[2]));//cost
                sb.append("\n");
                i++;
            }
            writer.write(sb.toString());
            System.out.println("Randomizing and Writing done!");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void ReadData() throws IOException {
        BufferedReader br = null;
        String line = "";
        int i=0;
        String cvsSplitBy = ",";
        try {
            br = new BufferedReader(new FileReader(nameofCSV));
            while ((line = br.readLine()) != null) {
                String[] csv_data_str = line.split(cvsSplitBy);
                csv_data[i][0]= Integer.parseInt(csv_data_str[0]);//weight
                csv_data[i][1]= Integer.parseInt(csv_data_str[1]);//size
                csv_data[i][2]= Integer.parseInt(csv_data_str[2]);//cost
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    System.out.println("Loading Done!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Individual getLargest(Individual[] pop){
        bIndividual = pop[0];
        int best_score = bIndividual.score;
        for(int i=1;i<pop_size;i++){
            if(best_score<=pop[i].score){
                best_score = pop[i].score;
                sbIndividual = bIndividual;
                bIndividual=pop[i];
            }
        }
        return bIndividual;
    }

    public static Individual tournament(Individual[] pop,int tour_size){
        Individual best_ind_tour = new Individual();
        int best_score = 0;
        Random rand = new Random();
        for (int i = 0; i < tour_size; i++) {
            int i1 = rand.nextInt(pop_size);
            if(best_score<pop[i1].score){
                best_ind_tour = pop[i1];
                best_score = pop[i1].score;
            }
        }
        return best_ind_tour;
    }
}
