call runDemo mceSort SeqMCEsort mceSeqData 1m-input.txt 1m-output-seq.txt
call runDemo mceSort SeqMCEsort mceSeqData 2m-input.txt 2m-output-seq.txt
call runDemo mceSort SeqMCEsort mceSeqData 4m-input.txt 4m-output-seq.txt
call runDemo mceSort SeqMCEsort mceSeqData 8m-input.txt 8m-output-seq.txt
call runDemo mceSort SeqMCEsort mceSeqData 16m-input.txt 16m-output-seq.txt
call runOnce mceSort ValidateSort mceSeqData 1m-output-seq.txt
call runOnce mceSort ValidateSort mceSeqData 2m-output-seq.txt
call runOnce mceSort ValidateSort mceSeqData 4m-output-seq.txt
call runOnce mceSort ValidateSort mceSeqData 8m-output-seq.txt
call runOnce mceSort ValidateSort mceSeqData 16m-output-seq.txt


