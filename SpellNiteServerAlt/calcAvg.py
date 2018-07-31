F = open("data.csv", "r")
a = F.readlines()
print(a)

xAvg = 0.0
zAvg = 0.0
counter = 0
for s in a:
    if counter != 0:
        nums = s.split(",")
        xAvg += float(nums[0])
        zAvg += float(nums[1])
    counter += 1
xAvg = xAvg/(len(a)-1)
zAvg = zAvg/(len(a)-1)
print(str(xAvg) + ", " + str(zAvg))