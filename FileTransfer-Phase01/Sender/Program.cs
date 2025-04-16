// Client

using System.Net.Sockets;
using System.Text.Json;
using Common;

await Task.Delay(3000); // wait till server starts

await Run_4();

static async Task Run_4()
{
    // v4: v3 code modified to send image and its metadata. working
    using TcpClient client = new("127.0.0.1", 13000);
    using NetworkStream channel = client.GetStream();
    using BinaryWriter channelWriter = new(channel);

    using FileStream fileStream = new(Constants.FilePath, FileMode.Open);
    using BinaryReader fileReader = new(fileStream);
    string filename = Path.GetFileName(Constants.FilePath);
    var metadataObj = new { filename, filesize = fileStream.Length };
    string metadata = JsonSerializer.Serialize(metadataObj);
    channelWriter.Write(metadata);
    for (int i = 0; i < fileStream.Length; i++)
    {
        byte readByte = fileReader.ReadByte();
        channelWriter.Write(readByte);
    }
    await Task.CompletedTask;
}
static async Task Run_3()
{
    // v3 : use binary writer to write image to stream. working correctly. (no metadata)
    using TcpClient client = new TcpClient("127.0.0.1", 13000);
    using NetworkStream channel = client.GetStream();
    using BinaryWriter channelWriter = new(channel);

    using FileStream fileStream = new(Constants.FilePath, FileMode.Open);
    using BinaryReader fileReader = new(fileStream);
    channelWriter.Write(fileStream.Length);
    for (int i = 0; i < fileStream.Length; i++)
    {
        byte readByte = fileReader.ReadByte();
        channelWriter.Write(readByte);
    }
    await Task.CompletedTask;
}
static async Task Run_2()
{
    // v2.1. working correctly
    using TcpClient client = new TcpClient("127.0.0.1", 13000);
    var channel = client.GetStream();

    using FileStream fileStream = new(Constants.FilePath, FileMode.Open);

    await fileStream.CopyToAsync(channel);
}