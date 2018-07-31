path = commandArgs(trailingOnly = TRUE)[1]
data = read.csv(paste(path, "data.csv", sep = ""), header = TRUE, sep = ",")
data.frame(data)
png(filename = paste(path, "hey.png", sep = ""))
plot(data)
dev.off()