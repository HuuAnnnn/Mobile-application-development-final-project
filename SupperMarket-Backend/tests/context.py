import sys
import os
from pathlib import Path

ROOT_PATH = Path(__file__).parent
sys.path.insert(0, os.path.join(ROOT_PATH, '..'))

import src
