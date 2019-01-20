import * as express from 'express';
import { Recoverable } from 'repl';
import * as mongoose from 'mongoose';

mongoose.connect('mongodb://hack:Hackatown2019@ds159574.mlab.com:59574/hackdb', {useNewUrlParser: true});

const Game = mongoose.model('Game', { name:String, public:Boolean });
const Player = mongoose.model('Player', {username: String, lat : Number, long: Number, tag: Boolean})

export class Index {
    public router = express.Router();

    constructor() {
        this.router.get('/', this.index.bind(this));
        this.router.post('/postGame', this.postGame.bind(this));
        this.router.get('/getGames', this.getAllGames.bind(this));
        this.router.get('/getPlayers', this.getPlayers.bind(this));
        this.router.delete('/deletePlayer/:username', this.deletePlayer.bind(this));
        this.router.post('/postPlayer', this.postPlayer.bind(this));
        this.router.put('/putPlayer/:username', this.putPlayer.bind(this));
    }

    private index(req: express.Request, res: express.Response) {
        res.json({
            version: process.env.VERSION
        });
    }

    private async postGame(req: express.Request, res: express.Response) {
        const exists = await Game.findOne({ name: req.body.name });
        if(exists){
            res.status(400).end();
            return;
        }
        const game = new Game(req.body);
        await game.save();
        res.end();
    }

    private async getAllGames(req: express.Request, res: express.Response) {
        res.send(await Game.find());
    }

    private async getPlayers(req: express.Request, res:express.Response) {
        res.send(await Player.find());
    }

    private async deletePlayer(req: express.Request, res:express.Response) {
        await Player.deleteOne({ username: req.params.username });
        res.end();
    }

    private async postPlayer(req:express.Request, res:express.Response) {
        const exists = await Player.findOne({ username: req.body.username });
        console.log(exists);
        if(exists){
            res.status(400).end();
            return;
        }
        const player = new Player(req.body);
        await player.save();
        res.end();
    }

    private async putPlayer(req:express.Request, res:express.Response){
        await Player.update({ username: req.params.username }, { $set: req.body});
        res.end();
    }
}
