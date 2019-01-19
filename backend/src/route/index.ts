import * as express from 'express';

export class Index {
    public router = express.Router();

    constructor() {
        this.router.get('/', this.index.bind(this));
        this.router.get('/test', this.test.bind(this));
    }

    private index(req: express.Request, res: express.Response) {
        res.json({
            version: process.env.VERSION
        });
    }

    private test(req: express.Request, res: express.Response) {
        res.send("test");
    }
}
