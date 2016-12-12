## For Python v2.7.5
##
## N=3: 40 configurations
## N=4: 454 configurations
## N=5: 13,098 configurations (0.56 seconds)
## N=6: 1,214,975 configurations (89.5 seconds)
## N=7: 110,483,315 configurations (3.60 hours)
## N=8:
##


from timeit import default_timer as timer


BigN=8
tile_count=BigN*(BigN+1)/2
board_size=BigN*(BigN+1)/2
attempts=0
solutions=0
tile_list=list()
board=list()


color_flag=True
CSI_BEG="\x1B["
CSI_END="m"
CSI_OFF=CSI_BEG+"0m"
CSI_black=CSI_BEG+"0;30;40"+CSI_END
CSI_red=CSI_BEG+"0;30;41"+CSI_END
CSI_green=CSI_BEG+"0;30;42"+CSI_END
CSI_blue=CSI_BEG+"0;30;44"+CSI_END
CSI_yellow=CSI_BEG+"0;30;43"+CSI_END
CSI_purple=CSI_BEG+"0;30;45"+CSI_END
CSI_teal=CSI_BEG+"0;30;46"+CSI_END
CSI_grey=CSI_BEG+"0;30;47"+CSI_END
CSI_COLORS=[CSI_black, CSI_red, CSI_green, CSI_blue, CSI_yellow, CSI_purple, CSI_teal, CSI_grey]


def build_tile_list():
	for outer in range(BigN):
		for inner in range(outer+1):
			tile=list()
			tile.append(outer+1)
			tile.append(False)
			tile_list.append(tile)


def unused_tiles():
	for index in range(tile_count):
		if tile_list[index][1]==False:
			return True
	return False


def init_board():
	for row in range(board_size):
		for col in range(board_size):
			position=list()
			position.append(row+1)
			position.append(col+1)
			position.append(0)
			position.append(False)
			board.append(position)


def show_board():
	for row in range(board_size-1, -1, -1):
		for col in range(board_size):
			position=board[(row*board_size)+col]
			
			if position[2]==0:
				print ".",
			else:
				if color_flag==True:
					print CSI_COLORS[position[2]-1]+str(position[2])+CSI_OFF,
				else:
					print position[2],
		print
	print


def add_drop_tile(tile_size, pos_row, pos_col, add_flag):
	for row in range(pos_row, pos_row+tile_size):
		for col in range(pos_col, pos_col+tile_size):
			board[(row*board_size)+col][3]=add_flag
			if add_flag==True:
				board[(row*board_size)+col][2]=tile_size
			else:
				board[(row*board_size)+col][2]=0


def can_fit(tile_size, pos_row, pos_col):
	if pos_row+tile_size>board_size or pos_col+tile_size>board_size:
		return False
	
	for row in range(pos_row, pos_row+tile_size):
		for col in range(pos_col, pos_col+tile_size):
			if board[(row*board_size)+col][3]==True:
				return False
	return True


def go_deep():
	global attempts
	attempts+=1
	global solutions
	
	for row in range(board_size):
		for col in range(board_size):
			if board[(row*board_size)+col][3]==False:
				last_tile_used=0
				for index in range(tile_count):
					tile=tile_list[index]
					if tile[1]==False and tile[0]!=last_tile_used:  # unused tile and a size we haven't already tried
						last_tile_used=tile[0]

						if can_fit(tile[0], row, col)==True:
							tile_list[index][1]=True
							add_drop_tile(tile[0], row, col, True)
						
							if unused_tiles()==True:
								go_deep()
							else:
								solutions+=1
								print "Solution #", solutions, "is configuration #", attempts
								show_board()  # don't return keep; looking for more solutions

							add_drop_tile(tile[0], row, col, False)
							tile_list[index][1]=False
				return  # bail if we've tried all tiles


def part_num():
	start=timer()
	build_tile_list()
	init_board()
	
	go_deep()
	end=timer()
	print "Tried", format(attempts, ",d"), "configurations in", (end-start)/(60*60), "hours"


part_num()


