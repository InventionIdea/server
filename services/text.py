import re

# the function takes a script and splits it into sentences
def split_script_into_sentences(script: str):
    sentences = re.split(r'(?<=[.!?]) +', script.strip())
    return sentences