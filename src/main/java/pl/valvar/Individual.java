package pl.valvar;

import java.util.Random;

public class Individual {
    public int[] chromoset = new int[GA_Knapsackj.num_obj];
    public int weight = 0;
    public int size = 0;
    public int score = 0 ;
    private static int ch_ind = GA_Knapsackj.num_obj/3;
    private static int ch_gen = 15;
    public static double one_probability = 0.20;

    public Individual() {
    }

    public void init(){
        Random rand = new Random();
        for (int i = 0; i < GA_Knapsackj.num_obj; i++) {
            if(Math.random()<one_probability){
                this.chromoset[i] = 1;
                one_probability-=0.1;
            }else{
                this.chromoset[i] = 0;
                one_probability+=0.025;
            }
        }
        evaluate();
    }

    public Individual crossover_operator(Individual parent1){
        Individual new_ind = new Individual();
        if(Math.random()<GA_Knapsackj.crossover_rate){
            for(int i=0;i<ch_ind;i++){
                new_ind.chromoset[i] = this.chromoset[i];
            }
            for(int i=ch_ind;i<GA_Knapsackj.num_obj;i++){
                new_ind.chromoset[i] = parent1.chromoset[i];
            }
            return new_ind;
        }
        return parent1;
    }

    public void mutate(){
        if(Math.random()<GA_Knapsackj.mutation_rate){
//            System.out.println("Mutation");
            for (int i = 0; i < ch_gen; i++) {
                int i1 = (int) (Math.random() * (GA_Knapsackj.num_obj - 1 + 1) + 0);
                if (this.chromoset[i1] == 0) {
                    this.chromoset[i1] = 1;
                } else {
                    this.chromoset[i1] = 0;
                }
            }
        }
    }

    public void evaluate(){
        this.score = 0;
        this.size = 0;
        this.weight = 0;
        for (int i = 1; i < GA_Knapsackj.num_obj; i++) {
            this.score += chromoset[i] * GA_Knapsackj.csv_data[i + 1][2];
            this.size += chromoset[i] * GA_Knapsackj.csv_data[i + 1][1];
            this.weight += chromoset[i] * GA_Knapsackj.csv_data[i + 1][0];
        }
        if (this.size>GA_Knapsackj.max_size || this.weight>GA_Knapsackj.max_cap){
            GA_Knapsackj.check+=1;
            this.score = 0;
        }
    }
}
