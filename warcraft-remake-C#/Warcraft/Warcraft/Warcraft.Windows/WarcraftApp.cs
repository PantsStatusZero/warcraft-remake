using SiliconStudio.Paradox.Engine;

namespace Warcraft
{
    class WarcraftApp
    {
        static void Main(string[] args)
        {
            using (var game = new Game())
            {
                game.Run();
            }
        }
    }
}
