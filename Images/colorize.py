m_dim = 36

from PIL import Image, ImageFont, ImageDraw

#
# These colors were chosen from Kenneth Kelly's 22 colors of maximum contrast.
# Read all about it here: http://hackerspace.kinja.com/some-os-x-calendar-tips-1658107833/1665644975
# And here's a handy table giving the RGB values: http://hackerspace.kinja.com/iscc-nbs-number-hex-r-g-b-263-f2f3f4-242-243-244-267-22-1665795040
#

color_list = ((34,34,34), (30,170,130), (0,103,165), (135,86,146), (161,202,241), (243,195,0), (190,0,50), (243,132,0))

def colorize(board_count):

    f = open('../8solutions.txt', 'r')

    square_px = 8
    board_px = square_px * m_dim + 1
    
    solution = 0;
    for loop in range(board_count):
        while (True):
            words = (f.readline()).split()
            if (len(words) > 0):
                if (words[0] == "Solution"):
                    break;
        
        im = Image.new('RGB', (board_px, board_px))
        dr = ImageDraw.Draw(im)
        
        for row in range(m_dim):
            line = f.readline().split()
            
            if (len(line) != m_dim):
                print "Wrong line length"
                f.close()
                return
            
            for i in range(m_dim):
                fill_color = color_list[int(line[i]) - 1]
                # dr.rectangle(((i*square_px, row*square_px), (i*square_px+square_px, row*square_px+square_px)), fill=fill_color, outline = "black")
                dr.rectangle(((i*square_px, row*square_px), (i*square_px+square_px, row*square_px+square_px)), fill=fill_color)
            
        solution = solution + 1
        file_name = str(solution).zfill(5) + ".png"
        im.save(file_name)

    f.close()



colorize(1021)


