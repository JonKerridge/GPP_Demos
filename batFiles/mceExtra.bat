call runDemo mceSort RunMCEsort mceParData 1 32m-input.txt 32m-output.txt
call runDemo mceSort RunMCEsort mceParData 2 32m-input.txt 32m-output.txt
call runDemo mceSort RunMCEsort mceParData 4 32m-input.txt 32m-output.txt
call runDemo mceSort RunMCEsort mceParData 8 32m-input.txt 32m-output.txt
call runDemo mceSort RunMCEsort mceParData 16 32m-input.txt 32m-output.txt
call runDemo mceSort SeqMCEsort mceSeqData 32m-input.txt 32m-output-seq.txt
call runOnce mceSort ValidateSort mceSeqData 32m-output.txt
call runOnce mceSort ValidateSort mceSeqData 32m-output-seq.txt
