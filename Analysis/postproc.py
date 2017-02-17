matrix = list()
m_dim = 36
trans_list = list()

def init_matrix():
    global matrix
    
    line = list()
    
    for i in range(m_dim):
        line.append(0.0)
    
    for i in range(m_dim):
        matrix.append(line)


def show_minmax(count):
    global matrix
    maxval = 0.0
    minval = float(count+1)
    
    for row in range(m_dim-1, -1, -1):
        for col in range(m_dim):
            value = matrix[row][col]
            if (count == 0):
                print "{0:03d}".format(int(value)),
            else:
                print "{0:.1f}".format(value/count),
                maxval = max(maxval, value/count)
                minval = min(minval, value/count)
        print
    print "Max:", maxval
    print "Min:", minval


def show_matrix(count):
    global matrix
    
    for row in range(m_dim-1, -1, -1):
        for col in range(m_dim):
            value = matrix[row][col]
            if (count == 0):
                print "{0:01d}".format(int(value)),
            else:
                print "{0:.1f}".format(value/count),
                maxval = max(maxval, value/count)
                minval = min(minval, value/count)
        print


def show_m_white(count):
    global matrix;
    
    for row in range(m_dim-1, -1, -1):
        for col in range(m_dim):
            if (count == 0):
                dispint = int(matrix[row][col])
                if (dispint == 0):
                    prstr = "   "
                elif (dispint < 10):
                    prstr = "  "+str(dispint)
                elif (dispint < 100):
                    prstr = " "+str(dispint)
                else:
                    prstr = str(dispint)
                print prstr,
            else:
                print "{0:.0f}".format(matrix[row][col]/count),
        print


def count_ones(board_count):
    global matrix

    init_matrix()

    f = open('8solutions.txt', 'r')
    
    for loop in range(board_count):
        while (True):
            words = (f.readline()).split()
            if (len(words) > 0):
                if (words[0] == "Solution"):
                    break;
        
        for row in range(m_dim):
            line = f.readline().split()
            
            if (len(line) != m_dim):
                print "Wrong line length"
                f.close()
                return
            
            mline = matrix[m_dim-row-1]
            for i in range(m_dim):
                if (float(line[i]) == 1.0):
                    line[i] = 1.0 + mline[i]
                else:
                    line[i] = 0.0 + mline[i]
            
            matrix[m_dim-row-1] = line
    
    show_matrix(0)
    f.close()

def tile_distrib(board_count):
    global matrix

    init_matrix()

    f = open('8solutions.txt', 'r')

    for loop in range(board_count):
        while (True):
            words = (f.readline()).split()
            if (len(words) > 0):
                if (words[0] == "Solution"):
                    break;
        
        for row in range(m_dim):
            line = f.readline().split()
            
            if (len(line) != m_dim):
                print "Wrong line length"
                f.close()
                return
            
            mline = matrix[m_dim-row-1]
            for i in range(m_dim):
                line[i] = float(line[i]) + mline[i]
            
            matrix[m_dim-row-1] = line
    
    show_minmax(board_count)
    f.close()


def trans_measure(board_count):
    global matrix

    init_matrix()

    f = open('8solutions.txt', 'r')

    sol_count = 0
    for loop in range(board_count):
        while (True):
            words = (f.readline()).split()
            if (len(words) > 0):
                if (words[0] == "Solution"):
                    break;
        
        transitions = 0
        for row in range(m_dim):
            line = f.readline().split()
            
            if (len(line) != m_dim):
                print "Wrong line length"
                f.close()
                return
            
            lastval = 0
            for i in range(m_dim):
                line[i] = int(line[i])
                if (lastval != line[i]):
                    lastval = line[i]
                    transitions = transitions + 1
            
            matrix[m_dim-row-1] = line

        for col in range(m_dim):
            lastval = 0
            for row in range(m_dim):
                if (lastval != matrix[row][col]):
                    lastval = matrix[row][col]
                    transitions = transitions + 1
        
        sol_count = sol_count + 1
        print "Solution #", sol_count, "has", transitions, "transitions"

    f.close()


def region_count(board_count):
    global matrix

    init_matrix()

    f = open('8solutions.txt', 'r')

    sol_count = 0
    for loop in range(board_count):
        while (True):
            words = (f.readline()).split()
            if (len(words) > 0):
                if (words[0] == "Solution"):
                    break;
        
        regions = 0
        for row in range(m_dim):
            line = f.readline().split()
            
            if (len(line) != m_dim):
                print "Wrong line length"
                f.close()
                return
            
            for i in range(m_dim):
                line[i] = int(line[i])
            
            matrix[m_dim-row-1] = line

        for row in range(m_dim):
            for col in range(m_dim):
                lastval = matrix[row][col]
        for col in range(m_dim):
            lastval = 0
            for row in range(m_dim):
                if (lastval != matrix[row][col]):
                    lastval = matrix[row][col]
                    transitions = transitions + 1
        
        sol_count = sol_count + 1
        print "Solution #", sol_count, "has", regions, "regions"

    f.close()



trans_measure(18656)




