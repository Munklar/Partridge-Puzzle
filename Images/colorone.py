m_dim = 36
line = [[0 for x in range(m_dim)] for y in range(m_dim)] 

from PIL import Image, ImageFont, ImageDraw

#
# These colors were chosen from Kenneth Kelly's 22 colors of maximum contrast.
# Read all about it here: http://hackerspace.kinja.com/some-os-x-calendar-tips-1658107833/1665644975
# And here's a handy table giving the RGB values: http://hackerspace.kinja.com/iscc-nbs-number-hex-r-g-b-263-f2f3f4-242-243-244-267-22-1665795040
#

color_list = ((34,34,34), (30,170,130), (0,103,165), (135,86,146), (161,202,241), (243,195,0), (190,0,50), (243,132,0))

def colorone():
    f = open('../tocolor.txt', 'r')

    square_px = 32 
    board_px = square_px * m_dim + 1
    
    for row in range(m_dim):
        line[row] = f.readline().split()
        
        if (len(line[row]) != m_dim):
            print "Wrong line length"
            f.close()
            return

    f.close()
        
    im = Image.new('RGB', (board_px, board_px))
    dr = ImageDraw.Draw(im)

    for row in range(m_dim):
        for col in range(m_dim):
            cell = int(line[row][col])

            if cell == 0:
                continue;

            dr.rectangle(((col*square_px, row*square_px), ((col + cell)*square_px, (row + cell)*square_px)), fill=color_list[cell - 1], outline = "black")

            for rowkill in range(cell):
                for colkill in range(cell):
                    line[row + rowkill][col + colkill] = '0'

    im.save("colorone.png")



colorone()


