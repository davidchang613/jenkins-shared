package functions

def getA() {
  echo 'getA() is called'
}

def setupFunction() {
  echo 'setupFunction()'
}

def showVersion() {
  sh 'node --version'
}
