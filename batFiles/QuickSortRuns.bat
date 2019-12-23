call runOnce QuickSortRecords FileRunTime qsData 1m-input.txt
call runOnce QuickSortRecords FileRunTime qsData 2m-input.txt
call runOnce QuickSortRecords FileRunTime qsData 4m-input.txt
call runOnce QuickSortRecords FileRunTime qsData 8m-input.txt
call runOnce QuickSortRecords FileRunTime qsData 16m-input.txt
call runDemo QuickSortRecords RunFileSort qsData 1 1m-input.txt 1m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 2 1m-input.txt 1m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 4 1m-input.txt 1m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 8 1m-input.txt 1m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 16 1m-input.txt 1m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 1 2m-input.txt 2m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 2 2m-input.txt 2m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 4 2m-input.txt 2m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 8 2m-input.txt 2m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 16 2m-input.txt 2m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 1 4m-input.txt 4m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 2 4m-input.txt 4m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 4 4m-input.txt 4m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 8 4m-input.txt 4m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 16 4m-input.txt 4m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 1 8m-input.txt 8m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 2 8m-input.txt 8m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 4 8m-input.txt 8m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 8 8m-input.txt 8m-output.txt
call runDemo QuickSortRecords RunFileSort qsData 16 8m-input.txt 8m-output.txt
call runDemo QuickSortRecords RunFileSort qsData2 1 16m-input.txt 16m-output.txt
call runDemo QuickSortRecords RunFileSort qsData2 2 16m-input.txt 16m-output.txt
call runDemo QuickSortRecords RunFileSort qsData2 4 16m-input.txt 16m-output.txt
call runDemo QuickSortRecords RunFileSort qsData2 8 16m-input.txt 16m-output.txt
call runDemo QuickSortRecords RunFileSort qsData2 16 16m-input.txt 16m-output.txt
call runOnce QuickSortRecords ValidateSort qsData 1m-output.txt
call runOnce QuickSortRecords ValidateSort qsData 2m-output.txt
call runOnce QuickSortRecords ValidateSort qsData 4m-output.txt
call runOnce QuickSortRecords ValidateSort qsData 8m-output.txt
call runOnce QuickSortRecords ValidateSort qsData 16m-output.txt


