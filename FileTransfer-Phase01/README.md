# FileTransfer (Phase 1)
This repo contains my 4 initial attempts in writing a client and server progams that can transfer an image and its metadata (filename and size only as string). The end goal is to learn network programming. The project has 4 versioned implementations, the first version is my very first atempt and it doesnt work at all. 

In this phase, I have achieved my file transfer goal using ```System.Net.Sockets.TCPClient``` and ```System.Net.Sockets.TCPListener``` directly without using HTTP. I learnt the difference between ```StreamWriter/StreamReader``` and ```BinaryWrtier/BinaryReader```. The former is specifically optimized to handle strings and strings only (AFAIK) while the latter can handle any binary data including images and strings. 

MIT No Attribution

Copyright 2025 A J Arun Kumar

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
